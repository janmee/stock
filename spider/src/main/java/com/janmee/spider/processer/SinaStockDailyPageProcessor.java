package com.janmee.spider.processer;

import com.alibaba.fastjson.JSONArray;
import com.janmee.spider.entity.Stock;
import com.janmee.spider.entity.StockDaily;
import com.janmee.spider.service.StockDailyService;
import com.janmee.spider.service.StockService;
import us.codecraft.webmagic.Page;
import us.codecraft.webmagic.Site;
import us.codecraft.webmagic.Spider;
import us.codecraft.webmagic.processor.PageProcessor;

import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * 获取stock表中代码的每天数据
 * @author luojianming on 2016/4/22.
 * @version 1.0
 */
public class SinaStockDailyPageProcessor implements PageProcessor {
    private Site site = Site.me().setRetryTimes(100).setSleepTime(100).setTimeOut(10000);
    private static StockService stockService = new StockService();
    private static StockDailyService stockDailyService = new StockDailyService();

    // process是定制爬虫逻辑的核心接口，在这里编写抽取逻辑
    public void process(Page page) {
        String requestUrl = page.getRequest().getUrl();
        String symbol = requestUrl.substring(requestUrl.lastIndexOf("=")+1,requestUrl.length());
        // 部分二：定义如何抽取页面信息，并保存下来
        Pattern pattern = Pattern.compile("var data=\\((.*)\\)");
        String rawText = page.getRawText();
        Matcher matcher = pattern.matcher(rawText);
        Stock stock = stockService.selectBySymbol(symbol);
        if (matcher.find()) {
            String jsonStr = matcher.group(1);
            System.out.println("json:" + jsonStr);
            List<StockDaily> stockDailies = JSONArray.parseArray(jsonStr, StockDaily.class);
            if (stockDailies != null && stockDailies.size() > 0) {
                stockDailyService.insertBatch(stockDailies, stock);
            }
        }
    }

    public Site getSite() {
        return site;
    }

    public static void main(String[] args) {
        List<Stock> stocks = stockService.selectAll();
        //设置要爬去的页面
        String[] request = new String[stocks.size()];
        for (int i = 0; i < stocks.size(); i++) {
            request[i] = "http://stock.finance.sina.com.cn/usstock/api/jsonp_v2.php/var%20data=/US_MinKService.getDailyK?symbol=" + stocks.get(i).getSymbol();
        }
        Spider.create(new SinaStockDailyPageProcessor()).addUrl(request).thread(6).run();
    }
}
