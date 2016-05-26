package com.janmee.stock.vo;

import java.util.List;

/**
 * @author luojianming on 2016/5/20.
 * @version 1.0
 */
public class DaySymbolVo {
    private String date;
    private List<StockProfit> stockProfits;

    public DaySymbolVo(String date, List<StockProfit> stockProfits) {
        this.date = date;
        this.stockProfits = stockProfits;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public List<StockProfit> getStockProfits() {
        return stockProfits;
    }

    public void setStockProfits(List<StockProfit> stockProfits) {
        this.stockProfits = stockProfits;
    }
}
