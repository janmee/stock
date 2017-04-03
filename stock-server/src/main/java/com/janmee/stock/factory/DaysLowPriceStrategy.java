package com.janmee.stock.factory;

import com.janmee.stock.base.utils.MapUtils;
import com.janmee.stock.entity.StockDaily;
import com.janmee.stock.utils.StockUtils;
import com.janmee.stock.vo.StragegyParam;
import com.seewo.core.util.collection.CollectionUtils;
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
public class DaysLowPriceStrategy extends BaseStrategy {

    /**
     * 策略2
     * 根据多天低价查找
     *
     * @param lowTimes 多天前价格与当前价倍数
     * @param todayStockDailies
     * @return
     */
    @Override
    public List<StockDaily> runStrategy(StragegyParam stragegyParam, List<StockDaily> todayStockDailies) {
        Date date = stragegyParam.getDate();
        int days = stragegyParam.getDays();
        double lowTimes = stragegyParam.getLowTimes();
        if (todayStockDailies == null || todayStockDailies.size() == 0) return new ArrayList<>();
        List<String> symbols = CollectionUtils.getPropertyList(todayStockDailies, "stockSymbol");
        //多天前数据
        Date lastWeekDay = StockUtils.getLastWeekDay(date, -days);
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
}
