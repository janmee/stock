package com.janmee.stock.scheduler;

import com.janmee.spider.processer.SinaStockDailyBetweenPageProcessor;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

/**
 * Created by Administrator on 2017/4/4.
 */
@Component
@EnableScheduling
public class DailyScanImpl implements DailyScan {
//    @Scheduled(cron = "*/5 * * * * ?")
    @Scheduled(fixedDelay = 5000)
    public void spiderDaily(){
        SinaStockDailyBetweenPageProcessor processor = new SinaStockDailyBetweenPageProcessor();
        processor.run();
        System.out.println("success");
    }
}
