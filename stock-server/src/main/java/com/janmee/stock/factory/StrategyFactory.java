package com.janmee.stock.factory;

import com.janmee.stock.vo.StragegyParam;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * @author luojianming on 2017/2/13.
 * @version 1.0
 */
@Component
public class StrategyFactory {

    @Autowired
    private static LongLowLineStragegy longLowLineStragegy;

    @Autowired
    private static OneDayVolumeLargeStrategy oneDayVolumeLargeStrategy;

    @Autowired
    private static DaysLowPriceStrategy daysLowPriceStrategy;

    @Autowired(required = true)
    public static void setLongLowLineStragegy(LongLowLineStragegy longLowLineStragegy) {
        StrategyFactory.longLowLineStragegy = longLowLineStragegy;
    }

    @Autowired(required = true)
    public static void setOneDayVolumeLargeStrategy(OneDayVolumeLargeStrategy oneDayVolumeLargeStrategy) {
        StrategyFactory.oneDayVolumeLargeStrategy = oneDayVolumeLargeStrategy;
    }

    @Autowired(required = true)
    public static void setDaysLowPriceStrategy(DaysLowPriceStrategy daysLowPriceStrategy) {
        StrategyFactory.daysLowPriceStrategy = daysLowPriceStrategy;
    }

    public static BaseStrategy  getInstant(int strategyId) {
        BaseStrategy baseStrategy = null;
        switch (strategyId) {
            case StragegyParam.ONE_DAY_VOLUME_LARGE:
                //根据当天成交量放大查找
                baseStrategy = oneDayVolumeLargeStrategy;
                break;
            case StragegyParam.DAYS_LOW_PRICE:
                //根据多天低价查找
                baseStrategy = daysLowPriceStrategy;
                break;
            case StragegyParam.LOW_LINE_RATE:
                //当天价格比昨天低，且当天出现长下影线
                baseStrategy = longLowLineStragegy;
                break;
        }
        return baseStrategy;
    }
}
