package com.janmee.stock.vo;


import com.janmee.stock.utils.DateUtils;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * @author luojianming on 2016/5/4.
 * @version 1.0
 */
public class StragegyParam {
    public static enum Type {
        OneDayVolumeLarge(1, "当天成交量放大"),
        DaysLowPrice(2, "多天低价"),
        LowlineRate(3,"低价并长下影线")
        ;
        private int type;
        private String desc;

        Type(int type, String desc) {
            this.type = type;
            this.desc = desc;
        }

        public int getType() {
            return type;
        }
    }

    public static final int ONE_DAY_VOLUME_LARGE = 1;
    public static final int DAYS_LOW_PRICE = 2;
    public static final int LOW_LINE_RATE = 3;

    private List<Integer> stragegyType;

    //计算日期
    private String date;

    //成交量放大倍数
    private Double times;

    //最低成交量
    private Long minVolume;

    //多天前数据
    private Integer days;

    private static StragegyParam defalutParam;

    private String endDate;

    //计算收益天数
    private Integer profitDays;

    //最低价与收盘价比例
    private Double lowlineRate;
    //当前价与多天前价格比率
    private Double lowTimes;

    public static StragegyParam defaultParam() {
        if (defalutParam == null) {
            defalutParam = new StragegyParam();
            defalutParam.setDate(DateUtils.formatDateStr(new Date(), DateUtils.PATTREN_DATE));
            defalutParam.setDays(20);
            defalutParam.setLowlineRate(0.5);
            defalutParam.setLowTimes(0.8);
            defalutParam.setMinVolume(500000L);
            defalutParam.setTimes(1.1);
            List<Integer> types = new ArrayList<>(3);
            types.add(1);
            types.add(2);
            types.add(3);
            defalutParam.setStragegyType(types);
        }
        return defalutParam;
    }

    public List<Integer> getStragegyType() {
        return stragegyType;
    }

    public void setStragegyType(List<Integer> stragegyType) {
        this.stragegyType = stragegyType;
    }

    public Integer getDays() {
        return days;
    }

    public void setDays(Integer days) {
        this.days = days;
    }

    public Double getLowTimes() {
        return lowTimes;
    }

    public void setLowTimes(Double lowTimes) {
        this.lowTimes = lowTimes;
    }

    public Date getDate() {
        return DateUtils.convertToDate(date);
    }

    public void setDate(String date) {
        this.date = date;
    }

    public Double getTimes() {
        return times;
    }

    public void setTimes(Double times) {
        this.times = times;
    }

    public Long getMinVolume() {
        return minVolume;
    }

    public void setMinVolume(Long minVolume) {
        this.minVolume = minVolume;
    }

    public String getEndDate() {
        return endDate;
    }

    public void setEndDate(String endDate) {
        this.endDate = endDate;
    }

    public Integer getProfitDays() {
        return profitDays;
    }

    public void setProfitDays(Integer profitDays) {
        this.profitDays = profitDays;
    }

    public Double getLowlineRate() {
        return lowlineRate;
    }

    public void setLowlineRate(Double lowlineRate) {
        this.lowlineRate = lowlineRate;
    }
}
