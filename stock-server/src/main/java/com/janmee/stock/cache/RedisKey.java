package com.janmee.stock.cache;

import com.janmee.stock.base.Constants;

/**
 * Redis Key 常量
 *
 * @author luojianming on 2016/3/4.
 * @version 1.0
 */
public class RedisKey {
    public static final String KEY_BASE = Constants.REDIS_PREFIX;

    public static final String KEY_DAILY_SYMBOL = Constants.REDIS_PREFIX + ":daily:%s";
}
