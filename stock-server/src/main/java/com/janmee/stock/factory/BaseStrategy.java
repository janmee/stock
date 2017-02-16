package com.janmee.stock.factory;

import com.janmee.stock.dao.StockDailyDao;
import com.janmee.stock.entity.StockDaily;
import com.janmee.stock.utils.StockUtils;
import com.janmee.stock.vo.StragegyParam;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.Date;
import java.util.List;

/**
 * @author luojianming on 2017/2/13.
 * @version 1.0
 */
public abstract class BaseStrategy {
    @Autowired
    protected StockDailyDao stockDailyDao;

    abstract public List<StockDaily> runStrategy(StragegyParam stragegyParam,List<StockDaily> stockDailies);

     /**
     * 获取日期的交易日数据
     */
     protected List<StockDaily> loopFindByDateAndStockSymbolIn(Date date, List<String> symbols) {
        Date lastWeekDay = date;
        List<StockDaily> oldStockDailies = stockDailyDao.findByDateAndStockSymbolInNative(lastWeekDay, symbols);
//        List<StockDaily> oldStockDailies = stockDailyDao.findByDateAndStockSymbolIn(lastWeekDay, symbols);
        int i = 0;
        while ((oldStockDailies == null || oldStockDailies.size() == 0) && i < 10) {
            lastWeekDay = StockUtils.getLastWeekDay(lastWeekDay, -1);
            //上一交易日数据
            oldStockDailies = stockDailyDao.findByDateAndStockSymbolInNative(lastWeekDay, symbols);
//            oldStockDailies = stockDailyDao.findByDateAndStockSymbolIn(lastWeekDay, symbols);
            i++;
//            oldStockDailies = findByDateAndStockSymbolInFromCache(lastWeekDay, symbols);
        }
        return oldStockDailies;
    }
}
