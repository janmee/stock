package com.janmee.stock.entity;

import com.alibaba.fastjson.annotation.JSONField;

import java.io.Serializable;

/**
 * @author luojianming on 2016/4/22.
 * @version 1.0
 */
public class StockDay implements Serializable{
    //英文名
    private String name;
    //中文名
    private String cname;
    //分类名
    private String category;
    //代码
    private String symbol;
    //现价
    private double price;
    //涨跌额
    private double diff;
    //涨跌幅
    private double chg;
    //昨收
    private double preclose;
    //今开
    private double open;
    //最高
    private double high;
    //最低
    private double low;
    //振幅
    private String amplitude;
    //成交量
    private long volume;
    //市值
    private long mktcap;
    //市盈率
    private double pe;
    //上市地
    private String market;
    //行业分类
    @JSONField(name = "category_id")
    private String categoryId;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getCname() {
        return cname;
    }

    public void setCname(String cname) {
        this.cname = cname;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public String getSymbol() {
        return symbol;
    }

    public void setSymbol(String symbol) {
        this.symbol = symbol;
    }

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    public double getDiff() {
        return diff;
    }

    public void setDiff(double diff) {
        this.diff = diff;
    }

    public double getChg() {
        return chg;
    }

    public void setChg(double chg) {
        this.chg = chg;
    }

    public double getPreclose() {
        return preclose;
    }

    public void setPreclose(double preclose) {
        this.preclose = preclose;
    }

    public double getOpen() {
        return open;
    }

    public void setOpen(double open) {
        this.open = open;
    }

    public double getHigh() {
        return high;
    }

    public void setHigh(double high) {
        this.high = high;
    }

    public double getLow() {
        return low;
    }

    public void setLow(double low) {
        this.low = low;
    }

    public String getAmplitude() {
        return amplitude;
    }

    public void setAmplitude(String amplitude) {
        this.amplitude = amplitude;
    }

    public long getVolume() {
        return volume;
    }

    public void setVolume(long volume) {
        this.volume = volume;
    }

    public long getMktcap() {
        return mktcap;
    }

    public void setMktcap(long mktcap) {
        this.mktcap = mktcap;
    }

    public double getPe() {
        return pe;
    }

    public void setPe(double pe) {
        this.pe = pe;
    }

    public String getMarket() {
        return market;
    }

    public void setMarket(String market) {
        this.market = market;
    }

    public String getCategoryId() {
        return categoryId;
    }

    public void setCategoryId(String categoryId) {
        this.categoryId = categoryId;
    }
}
