package com.janmee.stock.listener;

import com.janmee.stock.base.Constants;
import com.janmee.stock.cache.RedisCacheService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationListener;
import org.springframework.context.event.ContextRefreshedEvent;

/**
 * Redis缓存的初始化监听器
 */
public class RedisCacheInitListener implements ApplicationListener<ContextRefreshedEvent> {

    private Logger logger = LoggerFactory.getLogger(RedisCacheInitListener.class);

    @Autowired
    private RedisCacheService redisCacheService;

    @Override
    public void onApplicationEvent(ContextRefreshedEvent event) {
        logger.info("onApplicationEvent start");
        String pattern = Constants.REDIS_PREFIX + "*";
        logger.info("onApplicationEvent deleteKeyWithPattern by {}", pattern);
        //清空Redis缓存
        redisCacheService.deleteKeyWithPattern(pattern);
        logger.info("onApplicationEvent end");
    }
}