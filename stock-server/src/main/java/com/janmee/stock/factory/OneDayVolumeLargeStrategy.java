package com.janmee.stock.factory;

import com.janmee.stock.base.utils.MapUtils;
import com.janmee.stock.dao.StockDailyDao;
import com.janmee.stock.entity.StockDaily;
import com.janmee.stock.utils.StockUtils;
import com.janmee.stock.vo.StragegyParam;
import com.seewo.core.util.collection.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * @author luojianming on 2017/2/15.
 * @version 1.0
 */
@Component
public class OneDayVolumeLargeStrategy extends BaseStrategy {
    @Autowired
    private StockDailyDao stockDailyDao;

    /**
     * 策略1
     * 根据当天成交量放大查找
     * @param times 成交量放大倍数
     * @param minVolumn 最低成交量
     *
     * @return
     */
    @Override
    public List<StockDaily> runStrategy(StragegyParam stragegyParam, List<StockDaily> todayStockDailies) {
        Date date = stragegyParam.getDate();
        double times = stragegyParam.getTimes();
        long minVolume = stragegyParam.getMinVolume();
        if (todayStockDailies == null || todayStockDailies.size() == 0) return new ArrayList<>();
        List<String> symbols = CollectionUtils.getPropertyList(todayStockDailies, "stockSymbol");
        Date lastWeekDay = date;
        //昨天数据
        List<StockDaily> oldStockDailies = null;
        int i = 0;
        while ((oldStockDailies == null || oldStockDailies.size() == 0) && i < 7) {
            lastWeekDay = StockUtils.getLastWeekDay(lastWeekDay, -1);
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
}
