package com.janmee.spider.processer;

import com.alibaba.fastjson.JSONArray;
import com.janmee.spider.entity.Stock;
import com.janmee.spider.service.StockService;
import us.codecraft.webmagic.Page;
import us.codecraft.webmagic.Site;
import us.codecraft.webmagic.Spider;
import us.codecraft.webmagic.processor.PageProcessor;

import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * @author luojianming on 2016/4/22.
 * @version 1.0
 */
public class SinaStockPageProcessor implements PageProcessor {
    private static final int TOTAL_PAGE = 448;
    private Site site = Site.me().setRetryTimes(20).setSleepTime(100);
    private static StockService stockService = new StockService();

    // process是定制爬虫逻辑的核心接口，在这里编写抽取逻辑
    public void process(Page page) {
        // 部分二：定义如何抽取页面信息，并保存下来
        Pattern pattern = Pattern.compile("data:(.*)}\\)\\)");
        String rawText = page.getRawText();
        Matcher matcher = pattern.matcher(rawText);
        if (matcher.find()) {
            String jsonStr = matcher.group(1);
            System.out.println("json:" + jsonStr);
            List<Stock> stocks = JSONArray.parseArray(jsonStr, Stock.class);
            stockService.insertBatch(stocks);
        }
    }

    public Site getSite() {
        return site;
    }

    public static void main(String[] args) {
        //设置要爬去的页面
        String[] request = new String[TOTAL_PAGE];
        for (int i = 0; i < TOTAL_PAGE; i++) {
            request[i] = "http://stock.finance.sina.com.cn/usstock/api/jsonp.php//US_CategoryService.getList?page=" + (i + 1) + "&num=20&sort=&asc=0&market=&id=";
        }
        Spider.create(new SinaStockPageProcessor()).addUrl(request).thread(5).run();
    }
}
