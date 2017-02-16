package com.janmee.stock.factory;

import com.janmee.stock.vo.StragegyParam;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * @author luojianming on 2017/2/13.
 * @version 1.0
 */
public class StrategyFactory {

    @Autowired
    private static LongLowLineStragegy longLowLineStragegy;

    @Autowired
    private static OneDayVolumeLargeStrategy oneDayVolumeLargeStrategy;

    @Autowired
    private static DaysLowPriceStrategy daysLowPriceStrategy;


    public static BaseStrategy  getInstant(int strategyId) {
        BaseStrategy baseStrategy = null;
        switch (strategyId) {
            case StragegyParam.ONE_DAY_VOLUME_LARGE:
                baseStrategy = oneDayVolumeLargeStrategy;
                break;
            case StragegyParam.DAYS_LOW_PRICE:
                baseStrategy = daysLowPriceStrategy;
                break;
            case StragegyParam.LOW_LINE_RATE:
                baseStrategy = longLowLineStragegy;
                break;
        }
        return baseStrategy;
    }
}
