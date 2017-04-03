//package com.janmee.spider.shard;
//
//import com.google.code.shardbatis.strategy.ShardStrategy;
//import com.janmee.spider.entity.Stock;
//import com.janmee.spider.entity.StockDaily;
//
///**
// * @author luojianming on 2016/4/25.
// * @version 1.0
// */
//public class StockDailyShardStrategyImpl implements ShardStrategy {
//    @Override
//    public String getTargetTableName(String baseTableName, Object params, String mapperId) {
//        int i = 0;
//        if (params != null) {
//            StockDaily stockDaily = (StockDaily) params;
//            i = stockDaily.getStockId() % 10;
//        }
//        return baseTableName + "_" + i;
//    }
//}
