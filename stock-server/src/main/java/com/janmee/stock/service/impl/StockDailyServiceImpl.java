package com.janmee.stock.service.impl;

import com.janmee.stock.base.utils.MapUtils;
import com.janmee.stock.dao.StockDailyDao;
import com.janmee.stock.entity.StockDaily;
import com.janmee.stock.service.StockDailyService;
import com.janmee.stock.utils.DateUtils;
import com.janmee.stock.utils.EntityMapUtils;
import com.janmee.stock.vo.DaySymbolVo;
import com.janmee.stock.vo.StockProfit;
import com.janmee.stock.vo.StragegyParam;
import com.janmee.stock.vo.query.StockDailyQuery;
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
import javax.transaction.Transactional;
import java.text.ParseException;
import java.util.*;
import java.util.concurrent.*;

@Service
@Transactional
public class StockDailyServiceImpl implements StockDailyService {

    private static final Logger logger = LoggerFactory.getLogger(StockDailyServiceImpl.class);

    //20W
    private static final long MIN_VOLUME = 200000;

    @Autowired
    private StockDailyDao stockDailyDao;

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

        MyCallable(StragegyParam stragegyParam) {
            this.stragegyParam = stragegyParam;
        }

        @Override
        public Object call() throws Exception {
            List<StockDaily> stockDailies = runStragegy(stragegyParam);
            String date = DateUtils.formatDateStr(stragegyParam.getDate(), DateUtils.PATTREN_DATE);
            logger.debug(date);
            //计算收益
            List<StockProfit> stockProfits = calcProfit(stockDailies, stragegyParam.getDate(), stragegyParam.getDays());
            return new DaySymbolVo(date, stockProfits);
        }
    }


    public List<DaySymbolVo> scanAllDate(StragegyParam stragegyParam) {
        Date date1 = new Date();
        List<Integer> stragegyTypes = stragegyParam.getStragegyType();
        Date now = stragegyParam.getDate();
        Date endDate = DateUtils.convertToDate(stragegyParam.getEndDate());
        // 创建一个线程池
        ExecutorService pool = Executors.newFixedThreadPool(10);
        // 创建多个有返回值的任务
        List<Future> list = new ArrayList<Future>();
        for (now = getLastWeekDay(now, 0); now.compareTo(endDate) >= 0; now = getLastWeekDay(now, -1)) {
            StragegyParam newParam = new StragegyParam();
            BeanUtils.copyProperties(stragegyParam, newParam);
            newParam.setDate(DateUtils.formatDateStr(now, DateUtils.PATTREN_DATE));
            Callable c = new MyCallable(newParam);
            // 执行任务并获取Future对象
            Future f = pool.submit(c);
            // System.out.println(">>>" + f.get().toString());
            list.add(f);
        }
        // 关闭线程池
        pool.shutdown();
        List<DaySymbolVo> retList = new ArrayList<>();
        // 获取所有并发任务的运行结果
        for (Future f : list) {
            // 从Future对象上获取任务的返回值，并输出到控制台
            DaySymbolVo daySymbolVo = null;
            try {
                daySymbolVo = (DaySymbolVo) f.get();
                if (daySymbolVo.getStockProfits().size() > 0) {
                    retList.add(daySymbolVo);
                }
            } catch (InterruptedException e) {
                e.printStackTrace();
            } catch (ExecutionException e) {
                e.printStackTrace();
            }
        }

        Date date2 = new Date();
        System.out.println("----程序结束运行----，程序运行时间【"
                + (date2.getTime() - date1.getTime()) + "毫秒】");
        Collections.sort(retList, new Comparator<DaySymbolVo>() {
            @Override
            public int compare(DaySymbolVo o1, DaySymbolVo o2) {
                return o2.getDate().compareTo(o1.getDate());
            }
        });

        return retList;
    }

    private List<StockProfit> calcProfit(List<StockDaily> stockDailies, Date date, int days) {
        if (stockDailies != null && stockDailies.size() == 0) return new ArrayList<>();
        List<String> symbols = CollectionUtils.getPropertyList(stockDailies, "stockSymbol");
        Date lastWeekDay = getLastWeekDay(date, days);
        List<StockDaily> futureDailies = stockDailyDao.findByDateAndStockSymbolIn(lastWeekDay, symbols);
        Map<String, StockDaily> stockDailyMap = EntityMapUtils.toMap(stockDailies);
        List<StockProfit> stockProfits = new ArrayList<>();
        if (futureDailies != null && futureDailies.size() > 0) {
            StockProfit stockProfit = new StockProfit();
            for (StockDaily futureDaily : futureDailies) {
                StockDaily stockDaily = stockDailyMap.get(futureDaily.getStockSymbol());
                stockProfit.setSymbol(stockDaily.getStockSymbol());
                if (stockDaily.getCurrent() != 0)
                    stockProfit.setProfit((futureDaily.getCurrent() - stockDaily.getCurrent()) / stockDaily.getCurrent());
            }
            stockProfits.add(stockProfit);
        }
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
        //今天数据
        List<StockDaily> stockDailies = stockDailyDao.findByDate(now);
        if (stockDailies.size() != 0) {
            for (Integer type : stragegyTypes) {
                if (type == StragegyParam.Type.OneDayVolumeLarge.getType()) {
                    stockDailies = findByOneDayVolumeLarge(stragegyParam.getDate(), stragegyParam.getTimes(), stragegyParam.getMinVolume(), stockDailies);
                } else if (type == StragegyParam.Type.DaysLowPrice.getType()) {
                    stockDailies = findByDaysLowPrice(stragegyParam.getDate(), stragegyParam.getDays(), stragegyParam.getLowTimes(), stockDailies);
                }
            }
        }
        return stockDailies;
    }

    /**
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
            oldStockDailies = stockDailyDao.findByDateAndStockSymbolIn(lastWeekDay, symbols);
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
        return rets;
    }

    /**
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
        List<StockDaily> oldStockDailies = stockDailyDao.findByDateAndStockSymbolIn(lastWeekDay, symbols);
        while (oldStockDailies == null || oldStockDailies.size() == 0) {
            lastWeekDay = getLastWeekDay(lastWeekDay, -1);
            //上一交易日数据
            oldStockDailies = stockDailyDao.findByDateAndStockSymbolIn(lastWeekDay, symbols);
        }
        Map<String, StockDaily> oldMap = MapUtils.stockDailyToMap(oldStockDailies);
        List<StockDaily> rets = new ArrayList<>();
        for (StockDaily todayStockDaily : todayStockDailies) {
            if (oldMap.containsKey(todayStockDaily.getStockSymbol())) {
                StockDaily oldStockDaily = oldMap.get(todayStockDaily.getStockSymbol());
                if (todayStockDaily.getCurrent() != 0 && todayStockDaily.getCurrent() < lowTimes * oldStockDaily.getCurrent())
                    rets.add(todayStockDaily);
            }
        }
        return rets;
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
