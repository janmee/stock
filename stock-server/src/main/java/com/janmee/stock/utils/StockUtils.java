package com.janmee.stock.utils;

import java.text.ParseException;
import java.util.Calendar;
import java.util.Date;

/**
 * @author luojianming on 2017/2/15.
 * @version 1.0
 */
public class StockUtils {
    /**
     * 获取上一个交易日
     *
     * @return
     */
    public static Date getLastWeekDay(Date date, int last) {
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
