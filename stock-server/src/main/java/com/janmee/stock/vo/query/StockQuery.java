package com.janmee.stock.vo.query;

import com.seewo.core.base.BaseQuery;
import java.util.Date;
import org.apache.commons.lang.builder.ToStringBuilder;

public class StockQuery extends BaseQuery {
    
    /**
     * 
     */
    private Integer id;
    
    /**
     * 名字
     */
    private String name;
    
    /**
     * 中文名
     */
    private String cname;
    
    /**
     * 分类名
     */
    private String category;
    
    /**
     * 分类id
     */
    private String categoryId;
    
    /**
     * 代码
     */
    private String symbol;
    
    /**
     * mktcap
     */
    private String mktcap;
    
    /**
     * 市盈率
     */
    private Double pe;
    
    /**
     * 上市地
     */
    private String market;
    
    public Integer getId() {
        return id;
    }

    public void setId(Integer id){
        this.id = id;
    }
    
    public String getName() {
        return name;
    }

    public void setName(String name){
        this.name = name;
    }
    
    public String getCname() {
        return cname;
    }

    public void setCname(String cname){
        this.cname = cname;
    }
    
    public String getCategory() {
        return category;
    }

    public void setCategory(String category){
        this.category = category;
    }
    
    public String getCategoryId() {
        return categoryId;
    }

    public void setCategoryId(String categoryId){
        this.categoryId = categoryId;
    }
    
    public String getSymbol() {
        return symbol;
    }

    public void setSymbol(String symbol){
        this.symbol = symbol;
    }
    
    public String getMktcap() {
        return mktcap;
    }

    public void setMktcap(String mktcap){
        this.mktcap = mktcap;
    }
    
    public Double getPe() {
        return pe;
    }

    public void setPe(Double pe){
        this.pe = pe;
    }
    
    public String getMarket() {
        return market;
    }

    public void setMarket(String market){
        this.market = market;
    }
    
    @Override
    public String toString() {
        return ToStringBuilder.reflectionToString(this);
    }
}