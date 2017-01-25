package com.janmee.stock.service.impl;

import com.janmee.stock.base.StatusCode;
import com.janmee.stock.base.utils.MapUtils;
import com.janmee.stock.cache.RedisCacheService;
import com.janmee.stock.cache.RedisKey;
import com.janmee.stock.cache.RedisLock;
import com.janmee.stock.dao.StockDailyDao;
import com.janmee.stock.entity.StockDaily;
import com.janmee.stock.service.StockDailyService;
import com.janmee.stock.utils.DateUtils;
import com.janmee.stock.utils.EntityMapUtils;
import com.janmee.stock.vo.DaySymbolVo;
import com.janmee.stock.vo.ProfitCount;
import com.janmee.stock.vo.StockProfit;
import com.janmee.stock.vo.StragegyParam;
import com.janmee.stock.vo.query.StockDailyQuery;
import com.seewo.core.base.Constants;
import com.seewo.core.base.DataMap;
import com.seewo.core.util.bean.BeanUtils;
import com.seewo.core.util.bean.ObjectUtils;
import com.seewo.core.util.collection.CollectionUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import javax.persistence.criteria.*;
import java.text.ParseException;
import java.util.*;
import java.util.concurrent.*;
import java.util.concurrent.atomic.AtomicInteger;

@Service
public class StockDailyServiceImpl implements StockDailyService {

    private static final Logger logger = LoggerFactory.getLogger(StockDailyServiceImpl.class);

    //20W
    private static final long MIN_VOLUME = 200000;

    private static final int PAGE_SIZE = 2000;

    @Autowired
    private StockDailyDao stockDailyDao;

    @Autowired
    private RedisCacheService redisCacheService;

    private static AtomicInteger daoCount = new AtomicInteger(0);

    @Override
    public Page<StockDaily> findAll(Pageable pageable) {
        return stockDailyDao.findAll(pageable);
    }

    @Override
    public Page<StockDaily> findAll(StockDailyQuery query, Pageable pageable) {
        Specification<StockDaily> spec = this.buildSpecification(query);
        return stockDailyDao.findAll(spec, pageable);
    }

    @Override
    public StockDaily findOne(String id) {
        return stockDailyDao.findOne(id);
    }

    @Override
    public StockDaily create(StockDaily stockDaily) {
        return stockDailyDao.save(stockDaily);
    }

    @Override
    public StockDaily update(StockDaily stockDaily) {
        return stockDailyDao.save(stockDaily);
    }

    @Override
    public void delete(String id) {
        stockDailyDao.delete(id);
    }

    /**
     * 创建动态查询条件组合.
     */
    private Specification<StockDaily> buildSpecification(final StockDailyQuery query) {
        return new Specification<StockDaily>() {
            @Override
            public Predicate toPredicate(Root<StockDaily> root, CriteriaQuery<?> criteriaQuery, CriteriaBuilder criteriaBuilder) {
                List<Predicate> predicates = new ArrayList<Predicate>();
                if (ObjectUtils.isNotBlank(query.getStockId())) {
                    Path<String> stockId = root.get("stockId");
                    predicates.add(criteriaBuilder.equal(stockId, query.getStockId()));
                }
                if (ObjectUtils.isNotBlank(query.getStockCname())) {
                    Path<String> stockCname = root.get("stockCname");
                    predicates.add(criteriaBuilder.equal(stockCname, query.getStockCname()));
                }
                if (ObjectUtils.isNotBlank(query.getStockSymbol())) {
                    Path<String> stockSymbol = root.get("stockSymbol");
                    predicates.add(criteriaBuilder.equal(stockSymbol, query.getStockSymbol()));
                }
                if (ObjectUtils.isNotBlank(query.getDate())) {
                    Path<String> date = root.get("date");
                    predicates.add(criteriaBuilder.equal(date, query.getDate()));
                }
                if (ObjectUtils.isNotBlank(query.getOpen())) {
                    Path<String> open = root.get("open");
                    predicates.add(criteriaBuilder.equal(open, query.getOpen()));
                }
                if (ObjectUtils.isNotBlank(query.getHigh())) {
                    Path<String> high = root.get("high");
                    predicates.add(criteriaBuilder.equal(high, query.getHigh()));
                }
                if (ObjectUtils.isNotBlank(query.getLow())) {
                    Path<String> low = root.get("low");
                    predicates.add(criteriaBuilder.equal(low, query.getLow()));
                }
                if (ObjectUtils.isNotBlank(query.getCurrent())) {
                    Path<String> current = root.get("current");
                    predicates.add(criteriaBuilder.equal(current, query.getCurrent()));
                }
                if (ObjectUtils.isNotBlank(query.getVolume())) {
                    Path<String> volume = root.get("volume");
                    predicates.add(criteriaBuilder.equal(volume, query.getVolume()));
                }
                if (!predicates.isEmpty()) {
                    return criteriaBuilder.and(predicates.toArray(new Predicate[predicates.size()]));
                }
                return criteriaBuilder.conjunction();
            }
        };
    }

    class MyCallable implements Callable<Object> {
        private StragegyParam stragegyParam;
        private ProfitCount profitCount;


        MyCallable(StragegyParam stragegyParam, ProfitCount profitCount) {
            this.stragegyParam = stragegyParam;
            this.profitCount = profitCount;
        }


        @Override
        public Object call() throws Exception {
            List<StockDaily> stockDailies = runStragegy(stragegyParam);
            String date = DateUtils.formatDateStr(stragegyParam.getDate(), DateUtils.PATTREN_DATE);
            logger.debug(date);
            //计算收益
            List<StockProfit> stockProfits = calcProfit(stockDailies, stragegyParam.getDate(), stragegyParam.getProfitDays(), profitCount);
            return new DaySymbolVo(date, stockProfits);
        }
    }


    public DataMap scanAllDate(StragegyParam stragegyParam) {
        Date date1 = new Date();
        List<Integer> stragegyTypes = stragegyParam.getStragegyType();
        Date now = stragegyParam.getDate();
        Date endDate = DateUtils.convertToDate(stragegyParam.getEndDate());
        // 创建一个线程池
        ExecutorService pool = Executors.newFixedThreadPool(10);
        // 创建多个有返回值的任务
        List<Future> list = new ArrayList<Future>();
        ProfitCount profitCount = new ProfitCount();
        daoCount = new AtomicInteger(0);
        //每天一个线程
        for (now = getLastWeekDay(now, 0); now.compareTo(endDate) >= 0; now = getLastWeekDay(now, -1)) {
            StragegyParam newParam = new StragegyParam();
            BeanUtils.copyProperties(stragegyParam, newParam);
            newParam.setDate(DateUtils.formatDateStr(now, DateUtils.PATTREN_DATE));
            Callable c = new MyCallable(newParam, profitCount);
            // 执行任务并获取Future对象
            Future f = pool.submit(c);
            // System.out.println(">>>" + f.get().toString());
            list.add(f);
        }
        // 关闭线程池
        pool.shutdown();
        List<DaySymbolVo> retList = new ArrayList<>();
        // 获取所有并发任务的运行结果
        int i = 0;
        int total = 0;
        for (Future f : list) {
            // 从Future对象上获取任务的返回值，并输出到控制台
            DaySymbolVo daySymbolVo = null;
            try {
                i++;
                daySymbolVo = (DaySymbolVo) f.get();
                if (daySymbolVo.getStockProfits().size() > 0) {
                    retList.add(daySymbolVo);
                    total += daySymbolVo.getStockProfits().size();
                }
            } catch (InterruptedException e) {
                e.printStackTrace();
            } catch (ExecutionException e) {
                e.printStackTrace();
            }
        }
        pool.shutdownNow();

        Date date2 = new Date();
        logger.info("----程序结束运行----，" + i + "个线程运行时间【"
                + (date2.getTime() - date1.getTime()) + "毫秒】");
        Collections.sort(retList, new Comparator<DaySymbolVo>() {
            @Override
            public int compare(DaySymbolVo o1, DaySymbolVo o2) {
                return o2.getDate().compareTo(o1.getDate());
            }
        });
        DataMap dataMap = new DataMap();
        dataMap.addAttribute(Constants.DATA, retList);
        dataMap.addAttribute(Constants.STATUS_CODE, StatusCode.SUCCESS.getStatusCode());
        dataMap.addAttribute("daoCount", daoCount.intValue());
        dataMap.addAttribute("total", total);
        dataMap.addAttribute("profitCount", profitCount.getGainCount().intValue());
        dataMap.addAttribute("lossCount", profitCount.getLossCount().intValue());
        dataMap.addAttribute("avgProfit", ((double) profitCount.getTotalProfit().get()) / profitCount.getGainCount().intValue() / 1000);
        dataMap.addAttribute("avgLoss", ((double) profitCount.getTotalLoss().get()) / profitCount.getLossCount().intValue() / 1000);
        dataMap.addAttribute("totalAvg", ((double) profitCount.getTotalProfit().get() + (double) profitCount.getTotalLoss().get()) / (profitCount.getGainCount().intValue() + profitCount.getLossCount().intValue()) / 1000);
        return dataMap;
    }

    @Override
    public DataMap test(StragegyParam stragegyParam) {
        List<Integer> stragegyTypes = stragegyParam.getStragegyType();
        Date now = stragegyParam.getDate();
        // 创建一个线程池
        ExecutorService pool = Executors.newFixedThreadPool(10);
        // 创建多个有返回值的任务
        List<Future> list = new ArrayList<Future>();
        ProfitCount profitCount = new ProfitCount();
        daoCount = new AtomicInteger(0);
        //每天一个线程
        int i = 0;
//        for (now = getLastWeekDay(now, 0); i < 1000; now = getLastWeekDay(now, -1), i++) {
//            StragegyParam newParam = new StragegyParam();
//            BeanUtils.copyProperties(stragegyParam, newParam);
//            newParam.setDate(DateUtils.formatDateStr(now, DateUtils.PATTREN_DATE));
//            Callable c = new MyCallable(newParam, profitCount);
//            // 执行任务并获取Future对象
//            Future f = pool.submit(c);
//            // System.out.println(">>>" + f.get().toString());
//            list.add(f);
//        }
//        // 关闭线程池
//        pool.shutdown();
//        List<DaySymbolVo> retList = new ArrayList<>();
//        // 获取所有并发任务的运行结果
//        pool.shutdownNow();
//        // 获取所有并发任务的运行结果
//        int total = 0;
//        for (Future f : list) {
//            // 从Future对象上获取任务的返回值，并输出到控制台
//            DaySymbolVo daySymbolVo = null;
//            try {
//                daySymbolVo = (DaySymbolVo) f.get();
//            } catch (InterruptedException e) {
//                e.printStackTrace();
//            } catch (ExecutionException e) {
//                e.printStackTrace();
//            }
//        }
        StragegyParam newParam;
//        for (now = getLastWeekDay(now, 0); now.compareTo(endDate) >= 0; now = getLastWeekDay(now, -1)) {
        for (i = 0; i < 10000; i++) {
            newParam = new StragegyParam();
            BeanUtils.copyProperties(stragegyParam, newParam);
            stragegyParam.setDate(DateUtils.formatDateStr(now, DateUtils.PATTREN_DATE));
            List<StockDaily> stockDailies = runStragegy(stragegyParam);
            //计算收益
//            List<StockProfit> stockProfits = calcProfit(stockDailies, stragegyParam.getDate(), stragegyParam.getProfitDays(), profitCount);
//            stockDailyDao.findByDateNative(now);
            now = getLastWeekDay(now, -1);
//            List<StockDaily> stockDailies = runStragegy(stragegyParam);
//            String date = DateUtils.formatDateStr(now, DateUtils.PATTREN_DATE);
//            logger.debug(date);
            //计算收益
//            List<StockProfit> stockProfits = calcProfit(stockDailies, stragegyParam.getDate(), stragegyParam.getProfitDays(), profitCount);
//            list.add(new DaySymbolVo(date, stockProfits));
            System.out.println(i);
        }

        DataMap dataMap = new DataMap();
        dataMap.addAttribute("i", i);
//        dataMap.addAttribute(Constants.DATA, list);
//        dataMap.addAttribute(Constants.STATUS_CODE, StatusCode.SUCCESS.getStatusCode());
//        dataMap.addAttribute("daoCount", daoCount.intValue());
//        dataMap.addAttribute("profitCount", profitCount.getGainCount().intValue());
//        dataMap.addAttribute("lossCount", profitCount.getLossCount().intValue());
//        dataMap.addAttribute("avgProfit", ((double) profitCount.getTotalProfit().get()) / profitCount.getGainCount().intValue() / 1000);
//        dataMap.addAttribute("avgLoss", ((double) profitCount.getTotalLoss().get()) / profitCount.getLossCount().intValue() / 1000);
//        dataMap.addAttribute("totalAvg", ((double) profitCount.getTotalProfit().get() + (double) profitCount.getTotalLoss().get()) / (profitCount.getGainCount().intValue() + profitCount.getLossCount().intValue()) / 1000);
        return dataMap;
    }


    /**
     * 计算收益
     *
     * @param stockDailies 每日股价
     * @param date         当前日期
     * @param days         多少天后
     * @param profitCount  收益数
     * @return
     */
    private List<StockProfit> calcProfit(List<StockDaily> stockDailies, Date date, int days, ProfitCount profitCount) {
        if (stockDailies != null && stockDailies.size() == 0) return new ArrayList<>();
        List<String> symbols = CollectionUtils.getPropertyList(stockDailies, "stockSymbol");
        Date lastWeekDay = getLastWeekDay(date, days);
//        List<StockDaily> futureDailies = findByDateAndStockSymbolInFromCache(lastWeekDay, symbols);
        List<StockDaily> futureDailies = loopFindByDateAndStockSymbolIn(lastWeekDay, symbols);
        Map<String, StockDaily> stockDailyMap = EntityMapUtils.toMap(stockDailies);
        List<StockProfit> stockProfits = new ArrayList<>();
        if (CollectionUtils.isNotEmpty(futureDailies)) {
            for (StockDaily futureDaily : futureDailies) {
                StockProfit stockProfit = new StockProfit();
                if (futureDaily == null) continue;
                StockDaily stockDaily = stockDailyMap.get(futureDaily.getStockSymbol());
                stockProfit.setSymbol(stockDaily.getStockSymbol());
                if (stockDaily.getCurrent() != 0)
                    stockProfit.setProfit((futureDaily.getCurrent() - stockDaily.getCurrent()) / stockDaily.getCurrent());
                if (stockProfit.getProfit() >= 0) {
                    profitCount.getGainCount().incrementAndGet();
                    int profit = new Double(stockProfit.getProfit() * 1000).intValue();
                    profitCount.getTotalProfit().addAndGet(profit);
                } else if (stockProfit.getProfit() < 0) {
                    profitCount.getLossCount().incrementAndGet();
                    int loss = new Double(stockProfit.getProfit() * 1000).intValue();
                    profitCount.getTotalLoss().addAndGet(loss);
                }
                stockProfits.add(stockProfit);
            }
            futureDailies.clear();
        }
        stockDailyMap.clear();
        return stockProfits;
    }


    public List<String> findByStragegy(StragegyParam stragegyParam) {
        List<StockDaily> stockDailies = runStragegy(stragegyParam);
        List<String> symbols = CollectionUtils.getPropertyList(stockDailies, "stockSymbol");
        return symbols;
    }

    private List<StockDaily> runStragegy(StragegyParam stragegyParam) {
        List<Integer> stragegyTypes = stragegyParam.getStragegyType();
        Date now = stragegyParam.getDate();
        int total = stockDailyDao.countByDate(now);
        List<StockDaily> stockDailies;
        List<StockDaily> matchDailies = new ArrayList<>();
        for (int i = 0; i < total; i = i + PAGE_SIZE) {
            //今天数据
            stockDailies = stockDailyDao.findByDateNative(now, i, i + PAGE_SIZE);
//        List<StockDaily> stockDailies = stockDailyDao.findByDate(now);
            daoCount.incrementAndGet();
            if (stockDailies.size() != 0) {
                for (Integer type : stragegyTypes) {
                    if (type == StragegyParam.Type.OneDayVolumeLarge.getType()) {
                        stockDailies = findByOneDayVolumeLarge(stragegyParam.getDate(), stragegyParam.getTimes(), stragegyParam.getMinVolume(), stockDailies);
                    } else if (type == StragegyParam.Type.DaysLowPrice.getType()) {
                        stockDailies = findByDaysLowPrice(stragegyParam.getDate(), stragegyParam.getDays(), stragegyParam.getLowTimes(), stockDailies);
                    } else if (type == StragegyParam.Type.LowlineRate.getType()) {
                        stockDailies = findByLongLowLine(stragegyParam.getDate(), stragegyParam.getLowlineRate(), stockDailies);
                    }
                }
                matchDailies.addAll(stockDailies);
            }
        }
        return matchDailies;
    }

    /**
     * 策略1
     * 根据当天成交量放大查找
     *
     * @return
     */
    private List<StockDaily> findByOneDayVolumeLarge(Date date, Double times, Long minVolume, List<StockDaily> todayStockDailies) {
        if (todayStockDailies == null || todayStockDailies.size() == 0) return new ArrayList<>();
        List<String> symbols = CollectionUtils.getPropertyList(todayStockDailies, "stockSymbol");
        Date lastWeekDay = date;
        //昨天数据
        List<StockDaily> oldStockDailies = null;
        int i = 0;
        while ((oldStockDailies == null || oldStockDailies.size() == 0) && i < 7) {
            lastWeekDay = getLastWeekDay(lastWeekDay, -1);
            //上一交易日数据
            oldStockDailies = stockDailyDao.findByDateAndStockSymbolInNative(lastWeekDay, symbols);
//            oldStockDailies = stockDailyDao.findByDateAndStockSymbolIn(lastWeekDay, symbols);
//            oldStockDailies = findByDateAndStockSymbolInFromCache(lastWeekDay, symbols);
            i++;
        }
        if (oldStockDailies == null || oldStockDailies.size() == 0) return new ArrayList<>();
        Map<String, StockDaily> oldMap = MapUtils.stockDailyToMap(oldStockDailies);
        List<StockDaily> rets = new ArrayList<>();
        for (StockDaily todayStockDaily : todayStockDailies) {
            if (oldMap.containsKey(todayStockDaily.getStockSymbol())) {
                StockDaily oldStockDaily = oldMap.get(todayStockDaily.getStockSymbol());
                if (todayStockDaily.getVolume().longValue() > times * oldStockDaily.getVolume().longValue()
                        && todayStockDaily.getOpen() < oldStockDaily.getCurrent()
                        && todayStockDaily.getCurrent() > todayStockDaily.getLow()
                        && todayStockDaily.getVolume().longValue() > minVolume
                        )
                    rets.add(todayStockDaily);
            }
        }
        oldMap.clear();
        oldStockDailies.clear();
        return rets;
    }

    /**
     * 策略2
     * 根据多天低价查找
     *
     * @param todayStockDailies
     * @return
     */
    private List<StockDaily> findByDaysLowPrice(Date date, Integer days, Double lowTimes, List<StockDaily> todayStockDailies) {
        if (todayStockDailies == null || todayStockDailies.size() == 0) return new ArrayList<>();
        List<String> symbols = CollectionUtils.getPropertyList(todayStockDailies, "stockSymbol");
        //多天前数据
        Date lastWeekDay = getLastWeekDay(date, -days);
//        List<StockDaily> oldStockDailies = findByDateAndStockSymbolInFromCache(lastWeekDay, symbols);
        List<StockDaily> oldStockDailies = loopFindByDateAndStockSymbolIn(lastWeekDay, symbols);
        Map<String, StockDaily> oldMap = MapUtils.stockDailyToMap(oldStockDailies);
        List<StockDaily> rets = new ArrayList<>();
        for (StockDaily todayStockDaily : todayStockDailies) {
            if (oldMap.containsKey(todayStockDaily.getStockSymbol())) {
                StockDaily oldStockDaily = oldMap.get(todayStockDaily.getStockSymbol());
                if (todayStockDaily.getCurrent() != 0
                        && todayStockDaily.getCurrent() < lowTimes * oldStockDaily.getCurrent() //今天比前N天低lowTimes倍
                        )
                    rets.add(todayStockDaily);
            }
        }
        oldMap.clear();
        oldStockDailies.clear();
        return rets;
    }

    /**
     * 策略3
     * 当天价格比昨天低，且当天出现长下影线
     *
     * @param date              当天日期
     * @param lowlineRate       下影线，最低价比开盘价低多少，比率
     * @param todayStockDailies 当天数据
     * @return
     */
    private List<StockDaily> findByLongLowLine(Date date, Double lowlineRate, List<StockDaily> todayStockDailies) {
        if (todayStockDailies == null || todayStockDailies.size() == 0) return new ArrayList<>();
        List<String> symbols = CollectionUtils.getPropertyList(todayStockDailies, "stockSymbol");
        //上一个交易日数据
        Date lastWeekDay = getLastWeekDay(date, -1);
//        List<StockDaily> oldStockDailies = findByDateAndStockSymbolInFromCache(lastWeekDay, symbols);
        List<StockDaily> oldStockDailies = loopFindByDateAndStockSymbolIn(lastWeekDay, symbols);
        Map<String, StockDaily> oldMap = MapUtils.stockDailyToMap(oldStockDailies);
        List<StockDaily> rets = new ArrayList<>();
        double rate = 1 - lowlineRate;
        for (StockDaily todayStockDaily : todayStockDailies) {
            if (oldMap.containsKey(todayStockDaily.getStockSymbol())) {
                StockDaily oldStockDaily = oldMap.get(todayStockDaily.getStockSymbol());
                if (todayStockDaily.getCurrent() != 0
                        && todayStockDaily.getCurrent() < oldStockDaily.getCurrent() //今天比上一交易日低
                        && (todayStockDaily.getOpen() * rate >= todayStockDaily.getLow() //下影线
                        || todayStockDaily.getCurrent() * rate >= todayStockDaily.getLow())
                        )
                    rets.add(todayStockDaily);
            }
        }
        oldMap.clear();
        oldStockDailies.clear();
        return rets;
    }

    private List<StockDaily> findByDateAndStockSymbolInFromCache(Date date, List<String> symbols) {
        String dateStr = DateUtils.formatDateStr(date, DateUtils.PATTREN_DATE);
        String key = String.format(RedisKey.KEY_DAILY_SYMBOL, dateStr);
        List<StockDaily> stockDailies = null;
        if (redisCacheService.hasKey(key)) {
            stockDailies = (List<StockDaily>) (List<?>) redisCacheService.hget(key, (List<Object>) (List<?>) symbols);
        }
        if (CollectionUtils.isEmpty(stockDailies)) {
            RedisLock lock = redisCacheService.getLock(RedisKey.KEY_BASE + ":daily");
            try {
                if (lock.acquire()) {
                    if (redisCacheService.hasKey(key)) {
                        stockDailies = (List<StockDaily>) (List<?>) redisCacheService.hget(key, (List<Object>) (List<?>) symbols);
                    }
                    if (CollectionUtils.isEmpty(stockDailies)) {
                        //查找今天到前30天记录
                        Date lastWeekDay = getLastWeekDay(date, -200);
                        List<StockDaily> stockDailyList = stockDailyDao.findByDateBetweenOrderByDateDescNative(lastWeekDay, date);
//                        List<StockDaily> stockDailyList = stockDailyDao.findByDateBetweenOrderByDateDesc(lastWeekDay, date);
                        Map<String, Map<String, StockDaily>> map = toStockDailyMap(stockDailyList);
                        Set<Map.Entry<String, Map<String, StockDaily>>> entrySets = map.entrySet();
                        for (Map.Entry<String, Map<String, StockDaily>> entry : entrySets) {
                            redisCacheService.hput(String.format(RedisKey.KEY_DAILY_SYMBOL, entry.getKey()), (Map<String, Object>) (Map<String, ?>) entry.getValue());
                        }
                        stockDailies = (List<StockDaily>) (List<?>) redisCacheService.hget(key, (List<Object>) (List<?>) symbols);
                    }

                }
            } catch (InterruptedException e) {
                e.printStackTrace();
            } finally {
                lock.release();
            }
        }
        return stockDailies;


    }

    /**
     * 按照日期进行转换成map
     *
     * @return
     */
    private Map<String, Map<String, StockDaily>> toStockDailyMap(List<StockDaily> stockDailyList) {
        Map<String, Map<String, StockDaily>> map = new HashMap();
        Map<String, StockDaily> stockMap;
        String date;
        for (StockDaily stockDaily : stockDailyList) {
            date = DateUtils.formatDateStr(stockDaily.getDate(), DateUtils.PATTREN_DATE);
            if (!map.containsKey(date)) {
                stockMap = new HashMap();
            } else {
                stockMap = map.get(date);
            }
            stockMap.put(stockDaily.getStockSymbol(), stockDaily);
            map.put(date, stockMap);
        }
        return map;
    }

    /**
     * 获取日期的交易日数据
     */
    public List<StockDaily> loopFindByDateAndStockSymbolIn(Date date, List<String> symbols) {
        Date lastWeekDay = date;
        List<StockDaily> oldStockDailies = stockDailyDao.findByDateAndStockSymbolInNative(lastWeekDay, symbols);
//        List<StockDaily> oldStockDailies = stockDailyDao.findByDateAndStockSymbolIn(lastWeekDay, symbols);
        daoCount.incrementAndGet();
        int i = 0;
        while ((oldStockDailies == null || oldStockDailies.size() == 0) && i < 10) {
            lastWeekDay = getLastWeekDay(lastWeekDay, -1);
            //上一交易日数据
            oldStockDailies = stockDailyDao.findByDateAndStockSymbolInNative(lastWeekDay, symbols);
//            oldStockDailies = stockDailyDao.findByDateAndStockSymbolIn(lastWeekDay, symbols);
            daoCount.incrementAndGet();
            i++;
//            oldStockDailies = findByDateAndStockSymbolInFromCache(lastWeekDay, symbols);
        }
        return oldStockDailies;
    }


    /**
     * 获取上一个交易日
     *
     * @return
     */
    private Date getLastWeekDay(Date date, int last) {
        Date lastWeekDay = DateUtils.addDays(date, last);
        Calendar cal = Calendar.getInstance();
        cal.setTime(lastWeekDay);
        int w = cal.get(Calendar.DAY_OF_WEEK);
        switch (w) {
            case 7:
                lastWeekDay = DateUtils.addDays(lastWeekDay, -1);
                break;
            case 1:
                lastWeekDay = DateUtils.addDays(lastWeekDay, -2);
                break;
            default:
                break;
        }
        try {
            return DateUtils.formatDate(lastWeekDay, DateUtils.PATTREN_DATE);
        } catch (ParseException e) {
            e.printStackTrace();
            return new Date();
        }
    }
}
