package com.janmee.stock.vo.query;

import com.seewo.core.base.BaseQuery;
import java.util.Date;
import org.apache.commons.lang.builder.ToStringBuilder;

public class StockDailyQuery extends BaseQuery {
    
    /**
     * 
     */
    private Integer id;
    
    /**
     * 
     */
    private Integer stockId;
    
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
     * 成交量
     */
    private Integer volume;
    
    public Integer getId() {
        return id;
    }

    public void setId(Integer id){
        this.id = id;
    }
    
    public Integer getStockId() {
        return stockId;
    }

    public void setStockId(Integer stockId){
        this.stockId = stockId;
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
    
    public Integer getVolume() {
        return volume;
    }

    public void setVolume(Integer volume){
        this.volume = volume;
    }
    
    @Override
    public String toString() {
        return ToStringBuilder.reflectionToString(this);
    }
}