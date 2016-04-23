package com.janmee.stock.web.handler;

import org.aspectj.lang.JoinPoint;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.LinkedHashMap;
import java.util.Map;

/**
 * 输入输出日志记录
 * Created by luojianming on 2016/04/07.
 */
public class LogHandlerInterceptorAdapter {
    Logger log = LoggerFactory.getLogger(LogHandlerInterceptorAdapter.class);

    public void before(JoinPoint point) {
        Map map = new LinkedHashMap();
        if (point.getArgs() != null) {
            for (int i = 0; i < point.getArgs().length; i++) {
                map.put("parm" + i, point.getArgs()[i]);
            }
        }
        log.debug("Input==>{},param:{}", point.getSignature(), map.toString());
    }

    public void after(JoinPoint point, Object returnValue) {
        log.debug("Output==>{},param:{}", point.getSignature(), returnValue.toString());
    }
}
