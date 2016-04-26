package com.janmee.stock.entity;

import org.apache.commons.lang.builder.ToStringBuilder;
import org.hibernate.annotations.Where;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;
import java.math.BigInteger;
import java.util.Date;

@Entity
@Table(name = "stock_daily")
public class StockDaily extends IdEntity{
    
    /**
     * 
     */
    private Integer stockId;
    
    /**
     * 
     */
    private String stockCname;
    
    /**
     * 
     */
    private String stockSymbol;
    
    /**
     * 
     */
    private Date date;
    
    /**
     * 
     */
    private Double open;
    
    /**
     * 
     */
    private Double high;
    
    /**
     * 
     */
    private Double low;
    
    /**
     * 
     */
    private Double current;
    
    /**
     * 
     */
    private Long volume;
    
    public Integer getStockId() {
        return stockId;
    }

    public void setStockId(Integer stockId){
        this.stockId = stockId;
    }
    
    public String getStockCname() {
        return stockCname;
    }

    public void setStockCname(String stockCname){
        this.stockCname = stockCname;
    }
    
    public String getStockSymbol() {
        return stockSymbol;
    }

    public void setStockSymbol(String stockSymbol){
        this.stockSymbol = stockSymbol;
    }
    
    public Date getDate() {
        return date;
    }

    public void setDate(Date date){
        this.date = date;
    }
    
    public Double getOpen() {
        return open;
    }

    public void setOpen(Double open){
        this.open = open;
    }
    
    public Double getHigh() {
        return high;
    }

    public void setHigh(Double high){
        this.high = high;
    }
    
    public Double getLow() {
        return low;
    }

    public void setLow(Double low){
        this.low = low;
    }
    
    public Double getCurrent() {
        return current;
    }

    public void setCurrent(Double current){
        this.current = current;
    }
    
    public Long getVolume() {
        return volume;
    }

    public void setVolume(Long volume){
        this.volume = volume;
    }
    
    @Override
    public String toString() {
        return ToStringBuilder.reflectionToString(this);
    }
}