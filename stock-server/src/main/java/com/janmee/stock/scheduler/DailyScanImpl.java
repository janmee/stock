package com.janmee.stock.scheduler;

import com.janmee.spider.processer.SinaStockDailyBetweenPageProcessor;
import com.janmee.spider.processer.TodayStockPageProcessor;
import com.janmee.stock.service.StockDailyService;
import com.janmee.stock.utils.DateUtils;
import com.janmee.stock.vo.StragegyParam;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * Created by Administrator on 2017/4/4.
 */
@Component
@EnableScheduling
public class DailyScanImpl implements DailyScan {
    @Autowired
    private StockDailyService stockDailyService;

    @Scheduled(cron = "0 0 17 * * ?")
//    @Scheduled(fixedDelay = 5000)
    public void spiderDaily(){
        TodayStockPageProcessor.run();
        scanNotify();
    }

    //扫描并发送邮件
    private void scanNotify(){
        StragegyParam stragegyParam = StragegyParam.defaultParam();
        List<String>symbols = stockDailyService.findByStragegy(stragegyParam);
        stockDailyService.sendEmail(symbols, DateUtils.formatDateStr(stragegyParam.getDate(), DateUtils.PATTREN_DATE));
    }
}
