package com.janmee.stock.vo.query;

import com.seewo.core.util.date.DateUtils;

import java.util.Date;
import java.util.List;

/**
 * @author luojianming on 2016/5/4.
 * @version 1.0
 */
public class StragegyParam {
    public static enum Type {
        OneDayVolumeLarge(1, "当天成交量放大"),
        DaysLowPrice(2, "多天低价");
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

    private List<Integer> stragegyType;

    private String date;

    private Double times;

    private Long minVolume;

    //多天前数据
    private Integer days;

    //低于的倍数
    private Double lowTimes;

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
}
