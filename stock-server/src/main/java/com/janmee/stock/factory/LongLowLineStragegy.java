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

@Component
public class LongLowLineStragegy extends BaseStrategy {

    /**
     * 策略3
     * 当天价格比昨天低，且当天出现长下影线
     *
     * @param date              当天日期
     * @param lowlineRate       下影线，最低价比开盘价低多少，比率
     * @param todayStockDailies 当天数据
     * @return
     */
    @Override
    public List<StockDaily> runStrategy(StragegyParam stragegyParam, List<StockDaily> todayStockDailies) {
        Date date = stragegyParam.getDate();
        double lowlineRate = stragegyParam.getLowlineRate();
        if (todayStockDailies == null || todayStockDailies.size() == 0) return new ArrayList<>();
        List<String> symbols = CollectionUtils.getPropertyList(todayStockDailies, "stockSymbol");
        //上一个交易日数据
        Date lastWeekDay = StockUtils.getLastWeekDay(date, -1);
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
}
