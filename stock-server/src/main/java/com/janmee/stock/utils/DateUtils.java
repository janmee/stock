package com.janmee.stock.utils;

import org.springframework.util.Assert;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;

/**
 * @author luojianming on 2016/5/26.
 * @version 1.0
 */
public class DateUtils extends org.apache.commons.lang.time.DateUtils {
    public static final String PATTREN_DATE = "yyyy-MM-dd";
    public static final String PATTREN_PATH_DATE = "yyyy_MM";
    public static final String PATTREN_DATE_CN = "yyyy年MM月dd日";
    public static final String PATTREN_TIME = "HH:mm:ss";
    public static final String PATTREN_DATE_TIME = "yyyy-MM-dd HH:mm:ss";
    public static final String PATTREN_DATE_TIME2 = "yyyy-MM-dd HH:mm";
    public static DateFormat DATE_FORMAT_CN;
    public static DateFormat DATE_PATH_FORMAT;
    public static DateFormat TIME_FORMAT;
    public static DateFormat DATE_TIME_FORMAT;

    public DateUtils() {
    }

    public static Date getCurrentDateTime() {
        Calendar calNow = Calendar.getInstance();
        Date dtNow = calNow.getTime();
        return dtNow;
    }

    public static String getNowTime() {
        Date date = new Date();
        DATE_TIME_FORMAT = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.CHINA);
        String nowTime = DATE_TIME_FORMAT.format(date);
        return nowTime;
    }

    public static Date getToday() {
        return truncate(new Date(), 5);
    }

    public static Date getTodayEnd() {
        return getDayEnd(new Date());
    }

    public static Date convertToDate(String dateString) {
        try {
            DateFormat DATE_FORMAT = new SimpleDateFormat("yyyy-MM-dd", Locale.CHINA);
            return DATE_FORMAT.parse(dateString);
        } catch (ParseException var2) {
            return null;
        }
    }

    public static boolean checkDateString(String dateString) {
        return convertToDate(dateString) != null;
    }

    public static Date convertToDateTime(String dateTimeString) {
        try {
            DATE_TIME_FORMAT = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.CHINA);
            return DATE_TIME_FORMAT.parse(dateTimeString);
        } catch (ParseException var2) {
            return null;
        }
    }

    public static boolean checkDateTimeString(String dateTimeString) {
        return convertToDateTime(dateTimeString) != null;
    }

    public static Date getMonthEnd(int year, int month) {
        StringBuffer sb = new StringBuffer(10);
        Date date;
        if(month < 12) {
            sb.append(Integer.toString(year));
            sb.append("-");
            sb.append(month + 1);
            sb.append("-1");
            date = convertToDate(sb.toString());
        } else {
            sb.append(Integer.toString(year + 1));
            sb.append("-1-1");
            date = convertToDate(sb.toString());
        }

        date.setTime(date.getTime() - 1L);
        return date;
    }

    public static Date getMonthEnd(Date when) {
        Assert.notNull(when, "date must not be null !");
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(when);
        int year = calendar.get(1);
        int month = calendar.get(2) + 1;
        return getMonthEnd(year, month);
    }

    public static Date getDayEnd(Date when) {
        Date date = truncate(when, 5);
        date = addDays(date, 1);
        date.setTime(date.getTime() - 1L);
        return date;
    }

    public static Date getDay(Date when) {
        Date date = truncate(when, 5);
        return date;
    }

    public static Date add(Date when, int field, int amount) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(when);
        calendar.add(field, amount);
        return calendar.getTime();
    }

    public static Date addDays(Date when, int amount) {
        return add(when, 6, amount);
    }

    public static Date addMonths(Date when, int amount) {
        return add(when, 2, amount);
    }

    public static String getWeekDay(String Date, int dateValue, int flag) throws ParseException {
        Calendar calObj = Calendar.getInstance();
        SimpleDateFormat sfObj = new SimpleDateFormat("yyyy-MM-dd");
        calObj.setTime(sfObj.parse(Date));
        if(dateValue != 7) {
            if(flag == 1) {
                calObj.setFirstDayOfWeek(dateValue);
            } else {
                calObj.setFirstDayOfWeek(dateValue + 1);
            }
        }

        calObj.set(7, dateValue);
        return sfObj.format(calObj.getTime());
    }

    public static Date getMonth(String date, int flag) throws Exception {
        Calendar ca = Calendar.getInstance();
        SimpleDateFormat sfObj = new SimpleDateFormat("yyyy-MM-dd");
        ca.setTime(sfObj.parse(date));
        Date rtDate = null;
        if(flag == 1) {
            ca.set(5, 1);
            rtDate = ca.getTime();
        } else {
            ca.set(5, 1);
            rtDate = ca.getTime();
            ca.add(2, 1);
            ca.add(5, -1);
            rtDate = ca.getTime();
        }

        return rtDate;
    }

    public static String getStrMonth(String date, int flag) throws Exception {
        SimpleDateFormat sformatObj = new SimpleDateFormat("yyyy-MM-dd");
        Date returnStr = getMonth(date, flag);
        return sformatObj.format(returnStr);
    }

    public static int calMinutes(Date date1, Date date2) throws Exception {
        boolean times = false;
        int times1 = (int)((date1.getTime() - date2.getTime()) / 60000L);
        return times1;
    }

    public static Date calDay(Date date, int amount) {
        Calendar tempCalen = Calendar.getInstance();
        tempCalen.setTime(date);
        tempCalen.add(5, amount);
        return tempCalen.getTime();
    }

    public static Date dateAddition(Date date, String additStr) {
        Date reDate = null;
        String[] strs = additStr.split("\\:");
        if("m".equals(strs[0].toString())) {
            reDate = add(date, 12, Integer.parseInt(strs[1].toString()));
        } else if("h".equals(strs[0].toString())) {
            reDate = add(date, 11, Integer.parseInt(strs[1].toString()));
        } else if("d".equals(strs[0].toString())) {
            reDate = add(date, 5, Integer.parseInt(strs[1].toString()));
        }

        return reDate;
    }

    public static List<Date> getDatesOnWeek(Date startDate, Date endDate, int day) {
        ArrayList dates = new ArrayList();
        Calendar cal = Calendar.getInstance();
        Calendar endCal = Calendar.getInstance();
        endCal.setTime(endDate);
        cal.setTime(startDate);
        int startday = cal.get(7);
        if(startday != 0 && day != 0 && startday > day) {
            cal.add(3, 1);
        }

        cal.set(7, day);

        while(cal.compareTo(endCal) <= 0) {
            dates.add(cal.getTime());
            cal.add(3, 1);
        }

        return dates;
    }

    public static List<Date> getDatesOnDoubleWeek(Date startDate, Date endDate, int day) {
        ArrayList dates = new ArrayList();
        Calendar cal = Calendar.getInstance();
        Calendar endCal = Calendar.getInstance();
        endCal.setTime(endDate);
        cal.setTime(startDate);
        int startday = cal.get(7);
        if(startday != 0 && day != 0 && startday > day) {
            cal.add(3, 1);
        }

        cal.set(7, day);

        while(cal.compareTo(endCal) <= 0) {
            dates.add(cal.getTime());
            cal.add(3, 2);
        }

        return dates;
    }

    public static List<Date> getDatesOnMonth(Date startDate, Date endDate, int date) {
        ArrayList dates = new ArrayList();
        Calendar cal = Calendar.getInstance();
        Calendar endCal = Calendar.getInstance();
        endCal.setTime(endDate);
        cal.setTime(startDate);
        int startdate = cal.get(5);
        if(startdate > date) {
            cal.add(2, 1);
        }

        cal.set(5, date);

        while(cal.compareTo(endCal) <= 0) {
            dates.add(cal.getTime());
            cal.add(2, 1);
        }

        return dates;
    }

    public static int getCurrentWeekNum(Date startDate, Date endDate) {
        return 0;
    }

    public static Date parseDate(String dateStr, String pattern) throws ParseException {
        SimpleDateFormat df = new SimpleDateFormat(pattern);
        return df.parse(dateStr);
    }

    public static Date parseUSDate(String dateStr, String pattern) throws ParseException {
        SimpleDateFormat df = new SimpleDateFormat(pattern, Locale.US);
        return df.parse(dateStr);
    }

    public static synchronized Date formatDate(Date src, String pattern) throws ParseException {
        SimpleDateFormat df = new SimpleDateFormat(pattern);
        String dateStr = df.format(src);
        Date date = null;
        date = df.parse(dateStr);
        return date;
    }

    public static int getJavaDay(int day) {
        return day == 6?0:day + 1;
    }

    public static String convertDay2Week(Date day) {
        Calendar cal = Calendar.getInstance();
        cal.setTime(day);
        int iday = cal.get(7);
        String dayStr = "";
        if(iday == 1) {
            dayStr = "星期天";
        } else if(iday == 2) {
            dayStr = "星期一";
        } else if(iday == 3) {
            dayStr = "星期二";
        } else if(iday == 4) {
            dayStr = "星期三";
        } else if(iday == 5) {
            dayStr = "星期四";
        } else if(iday == 6) {
            dayStr = "星期五";
        } else if(iday == 7) {
            dayStr = "星期六";
        }

        return dayStr;
    }

    public static long getDateDiffNum(String d1, String d2) throws ParseException {
        DateFormat DATE_FORMAT = new SimpleDateFormat("yyyy-MM-dd", Locale.CHINA);
        return getDateDiffNum((Date)DATE_FORMAT.parse(d1), (Date)DATE_FORMAT.parse(d2));
    }

    public static long getDateDiffNum(Date startDate, Date endDate) {
        long result = (endDate.getTime() - startDate.getTime()) / 86400000L;
        return result;
    }

    public static long getCurrentYear() {
        Calendar cal = Calendar.getInstance();
        return (long)cal.get(1);
    }

    public static long getCurrentMonth() {
        Calendar cal = Calendar.getInstance();
        return (long)cal.get(2);
    }

    public static long getCurrentDate() {
        Calendar cal = Calendar.getInstance();
        return (long)cal.get(5);
    }

    public static long getDate(Date date) {
        Calendar cal = Calendar.getInstance();
        cal.setTime(date);
        return (long)cal.get(6);
    }

    public static String formatDateStr(Date src, String pattern) {
        SimpleDateFormat df = new SimpleDateFormat(pattern);
        String dateStr = df.format(src);
        return dateStr;
    }

    public static Date getNextDay(Date startTime, int n) {
        GregorianCalendar calendar = new GregorianCalendar();
        calendar.setTime(startTime);
        calendar.add(5, n);
        return calendar.getTime();
    }

    public static boolean isOverlap(Date leftStartDate, Date leftEndDate, Date rightStartDate, Date rightEndDate) {
        Assert.notNull(leftStartDate, "leftStartDate is required; it must not be null");
        Assert.notNull(leftEndDate, "leftEndDate is required; it must not be null");
        Assert.notNull(rightStartDate, "rightStartDate is required; it must not be null");
        Assert.notNull(rightEndDate, "rightEndDate is required; it must not be null");
        Assert.isTrue(!leftStartDate.after(leftEndDate), "leftStartDate must before leftEndDate");
        Assert.isTrue(!rightStartDate.after(rightEndDate), "rightStartDate must before rightEndDate");
        boolean isOverlap = !leftStartDate.before(rightStartDate) && leftStartDate.before(rightEndDate) || leftStartDate.after(rightStartDate) && !leftStartDate.after(rightEndDate) || !rightStartDate.before(leftStartDate) && rightStartDate.before(leftEndDate) || rightStartDate.after(leftStartDate) && !rightStartDate.after(leftEndDate);
        return isOverlap;
    }

    public static String getStrTime(Date date) {
        Calendar calendar = Calendar.getInstance();
        int currentYear = calendar.get(1);
        int currentDayOfYear = calendar.get(6);
        calendar.setTime(date);
        int year = calendar.get(1);
        int dayOfYear = calendar.get(6);
        DateFormat TIME_FORMAT = new SimpleDateFormat("HH:mm:ss", Locale.CHINA);
        return year == currentYear && currentDayOfYear == dayOfYear?"今天 " + TIME_FORMAT.format(date):DATE_TIME_FORMAT.format(date);
    }

    public static void main(String[] args) throws Exception {
        System.out.println(getDate(new Date()));
    }

}
