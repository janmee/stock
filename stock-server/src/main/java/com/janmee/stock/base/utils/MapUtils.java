package com.janmee.stock.base.utils;

import com.janmee.stock.entity.StockDaily;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author luojianming on 2016/4/26.
 * @version 1.0
 */
public class MapUtils {
    /**
     *
     * @param stockDailies
     * @return Map<symbol,StockDaily>
     */
    public static Map<String,StockDaily> stockDailyToMap(List<StockDaily>stockDailies){
        Map<String,StockDaily> map = new HashMap<>();
        for (StockDaily stockDaily : stockDailies){
            map.put(stockDaily.getStockSymbol(),stockDaily);
        }
        return map;
    }
}
