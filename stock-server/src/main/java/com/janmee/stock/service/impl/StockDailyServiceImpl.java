package com.janmee.stock.service.impl;

import com.janmee.stock.base.utils.MapUtils;
import com.janmee.stock.dao.StockDailyDao;
import com.janmee.stock.entity.StockDaily;
import com.janmee.stock.service.StockDailyService;
import com.janmee.stock.vo.query.StockDailyQuery;
import com.seewo.core.util.bean.ObjectUtils;
import com.seewo.core.util.date.DateUtils;
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


    public List<String> findByStragegy(Date date, Double times ,Long minVolume) {
        Date now = date;
        //今天数据
        List<StockDaily> todayStockDailies = stockDailyDao.findByDate(now);
        Date lastWeekDay = getLastWeekDay(now,-1);
        //昨天数据
        List<StockDaily> oldStockDailies = stockDailyDao.findByDate(lastWeekDay);
        Map<String, StockDaily> oldMap = MapUtils.stockDailyToMap(oldStockDailies);
        List<String> symbols = new ArrayList<>();
        for (StockDaily todayStockDaily : todayStockDailies) {
            if (oldMap.containsKey(todayStockDaily.getStockSymbol())) {
                StockDaily oldStockDaily = oldMap.get(todayStockDaily.getStockSymbol());
                if (todayStockDaily.getVolume().longValue() > times * oldStockDaily.getVolume().longValue()
                        && todayStockDaily.getOpen() < oldStockDaily.getCurrent()
                        && todayStockDaily.getCurrent() > todayStockDaily.getLow()
                        && todayStockDaily.getVolume().longValue() > minVolume
                        )
                    symbols.add(todayStockDaily.getStockSymbol());
            }
        }
        return symbols;
    }

    /**
     * 获取上一个交易日
     *
     * @return
     */
    private Date getLastWeekDay(Date date,int last) {
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
            return DateUtils.formatDate(lastWeekDay, "yyyy-MM-DD");
        } catch (ParseException e) {
            e.printStackTrace();
            return new Date();
        }
    }
}
