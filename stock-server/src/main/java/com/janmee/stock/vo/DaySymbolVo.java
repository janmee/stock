package com.janmee.stock.vo;

import java.util.List;

/**
 * @author luojianming on 2016/5/20.
 * @version 1.0
 */
public class DaySymbolVo {
    private String date;
    private List<String> symbols;

    public DaySymbolVo(String date, List<String> symbols) {
        this.date = date;
        this.symbols = symbols;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public List<String> getSymbols() {
        return symbols;
    }

    public void setSymbols(List<String> symbols) {
        this.symbols = symbols;
    }
}
