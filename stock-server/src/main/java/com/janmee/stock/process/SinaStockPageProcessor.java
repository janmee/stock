package com.janmee.stock.process;

import com.alibaba.fastjson.JSONArray;
import com.janmee.stock.entity.StockDay;
import us.codecraft.webmagic.Page;
import us.codecraft.webmagic.Site;
import us.codecraft.webmagic.processor.PageProcessor;

import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * @author luojianming on 2016/4/22.
 * @version 1.0
 */
public class SinaStockPageProcessor implements PageProcessor {
    private static final int TOTAL_PAGE = 1;
    private Site site = Site.me().setRetryTimes(3).setSleepTime(0);

    @Override
    // process是定制爬虫逻辑的核心接口，在这里编写抽取逻辑
    public void process(Page page) {
        // 部分二：定义如何抽取页面信息，并保存下来
//        List<String> request = new ArrayList<String>();
//        for (int i = 1; i <= TOTAL_PAGE; i++) {
//            request.add("http://stock.finance.sina.com.cn/usstock/api/jsonp.php//US_CategoryService.getList?page=" + i + "&num=20&sort=&asc=0&market=&id=");
//        }
//        page.addTargetRequests(request);
        Pattern pattern = Pattern.compile("data:(.*)}\\)\\)");
        String rawText = page.getRawText();
        Matcher matcher = pattern.matcher(rawText);
        if (matcher.find()){
            String jsonStr = matcher.group(1);
            List<StockDay> stockDay = JSONArray.parseArray(jsonStr, StockDay.class);
            stockDay.get(0);
        }



    }

    @Override
    public Site getSite() {
        return site;
    }

    public static void main(String[] args) {
//        Spider.create(new SinaStockPageProcessor()).addUrl("http://stock.finance.sina.com.cn/usstock/api/jsonp.php//US_CategoryService.getList?page=1&num=20&sort=&asc=0&market=&id=").thread(5).run();
    }
}
