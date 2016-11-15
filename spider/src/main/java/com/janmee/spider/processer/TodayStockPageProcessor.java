package com.janmee.spider.processer;

import com.alibaba.fastjson.JSONArray;
import com.janmee.spider.entity.Stock;
import com.janmee.spider.entity.StockDaily;
import com.janmee.spider.entity.StockDay;
import com.janmee.spider.service.StockDailyService;
import com.janmee.spider.service.StockService;
import com.seewo.core.util.bean.BeanUtils;
import com.seewo.core.util.date.DateUtils;
import us.codecraft.webmagic.Page;
import us.codecraft.webmagic.Site;
import us.codecraft.webmagic.Spider;
import us.codecraft.webmagic.processor.PageProcessor;

import java.text.ParseException;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * 上一个交易日数据
 * @author luojianming on 2016/4/22.
 * @version 1.0
 */
public class TodayStockPageProcessor implements PageProcessor {
    private static final int TOTAL_PAGE = 150;
    private Site site = Site.me().setRetryTimes(100).setSleepTime(100).setTimeOut(10000);
    private static StockService stockService = new StockService();
    private static StockDailyService stockDailyService = new StockDailyService();
    private static Map<String, Stock> allSymbols = null;

    // process是定制爬虫逻辑的核心接口，在这里编写抽取逻辑
    public void process(Page page) {
        // 部分二：定义如何抽取页面信息，并保存下来
        Pattern pattern = Pattern.compile("data:(.*)}\\)\\)");
        String rawText = page.getRawText();
        Matcher matcher = pattern.matcher(rawText);
        Date lastDay = getLastWeekDay(new Date(), -1);
        if (matcher.find()) {
            String jsonStr = matcher.group(1);
            System.out.println("json:" + jsonStr);
            List<StockDay> stockDays = JSONArray.parseArray(jsonStr, StockDay.class);
            List<StockDaily> stockDailies = new ArrayList<StockDaily>();
            for (StockDay stockDay : stockDays) {
                Stock stock = new Stock();
                BeanUtils.copyProperties(stockDay, stock);
                StockDaily stockDaily = new StockDaily();
                BeanUtils.copyProperties(stockDay, stockDaily);
                stockDaily.setStockCname(stockDay.getCname());
                stockDaily.setStockSymbol(stockDay.getSymbol());
                stockDaily.setDate(lastDay);
                stockDaily.setCurrent(stockDay.getPrice());
                if (!allSymbols.containsKey(stockDay.getSymbol())) {
                    stockService.insertStock(stock);
                    stockDaily.setStockId(stock.getId());
                } else {
                    stockDaily.setStockId(allSymbols.get(stockDay.getSymbol()).getId());
                }
                stockDailies.add(stockDaily);
            }
            stockDailyService.insertBatch(stockDailies);
        }
    }

    public Site getSite() {
        return site;
    }

    public static void main(String[] args) {
        List<Stock> stocks = stockService.selectAll();
        allSymbols = new HashMap<String, Stock>();
        for (Stock stock : stocks) {
            allSymbols.put(stock.getSymbol(), stock);
        }
        //设置要爬去的页面
        String[] request = new String[TOTAL_PAGE];
        for (int i = 0; i < TOTAL_PAGE; i++) {
            request[i] = "http://stock.finance.sina.com.cn/usstock/api/jsonp.php//US_CategoryService.getList?page=" + (i + 1) + "&num=60";
        }
        Spider.create(new TodayStockPageProcessor()).addUrl(request).thread(5).run();
    }

    /**
     * 获取上一个交易日
     *
     * @return
     */
    private Date getLastWeekDay(Date date, int last) {
        Date lastWeekDay = DateUtils.addDays(date, last);
        Calendar cal = Calendar.getInstance();
        cal.setTime(lastWeekDay);
        int w = cal.get(Calendar.DAY_OF_WEEK);
        switch (w) {
            case 7:
                lastWeekDay = DateUtils.addDays(lastWeekDay, -1);
                break;
            case 1:
                lastWeekDay = DateUtils.addDays(lastWeekDay, -2);
                break;
            default:
                break;
        }
        try {
            return DateUtils.formatDate(lastWeekDay, DateUtils.PATTREN_DATE);
        } catch (ParseException e) {
            e.printStackTrace();
            return new Date();
        }
    }
}
