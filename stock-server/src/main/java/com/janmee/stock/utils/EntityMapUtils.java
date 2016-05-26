package com.janmee.stock.utils;

import com.janmee.stock.entity.StockDaily;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author luojianming on 2016/5/26.
 * @version 1.0
 */
public class EntityMapUtils {
    public static Map<String, StockDaily> toMap(List<StockDaily> stockDailies) {
        Map map = new HashMap<>();
        for (StockDaily stockDaily : stockDailies) {
            map.put(stockDaily.getStockSymbol(), stockDaily);
        }
        return map;
    }
}
