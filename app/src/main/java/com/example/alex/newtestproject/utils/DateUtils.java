package com.example.alex.newtestproject.utils;

import java.sql.Timestamp;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

public class DateUtils {
    private static final String TAG = DateUtils.class.getSimpleName();

    public static Date getSystemTime() {
        return getSystemTime("1");
    }

    public static String getStrSystemTime() {
        return getStrSystemTime("1");
    }

    public static int getCurrentMonth() {
        Calendar instance = Calendar.getInstance(Locale.CHINA);
        return instance.get(Calendar.MONTH);
    }

    public static int getYear() {
        Calendar instance = Calendar.getInstance(Locale.CHINA);
        return instance.get(Calendar.YEAR);
    }

    public static String getCurrent(String format) {
        Calendar instance = Calendar.getInstance(Locale.CHINA);
        SimpleDateFormat sdf = new SimpleDateFormat(format);
        return sdf.format(instance.getTime());
    }

    public static String getStringMonth() {
        Calendar instance = Calendar.getInstance(Locale.CHINA);
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM");
        return sdf.format(instance.getTime());
    }

    public static String[] generateYearMonth() {
        Calendar instance = Calendar.getInstance(Locale.CHINA);
        int month = instance.get(Calendar.MONTH);
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy年MM月");
        String[] months = new String[month + 14];
        months[0] = "全部";
        for (int i = 1; i < months.length; i++) {
            if (i > 1) {
                instance.add(Calendar.MONTH, -1);
            }
            months[i] = sdf.format(instance.getTime());
        }
        return months;
    }

    public static String[] generateExeYearMonth() {
        Calendar instance = Calendar.getInstance(Locale.CHINA);
        int month = instance.get(Calendar.MONTH);
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM");
        String[] months = new String[month + 14];
        months[0] = "";
        for (int i = 1; i < months.length; i++) {
            if (i > 1) {
                instance.add(Calendar.MONTH, -1);
            }
            months[i] = sdf.format(instance.getTime());
        }
        return months;
    }

    public static String[] generateYears() {
        String[] years = new String[3];
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy年");
        Calendar instance = Calendar.getInstance(Locale.CHINA);
        for (int i = 0; i < years.length; i++) {
            if (i != 0) {
                instance.add(Calendar.YEAR, -1);
            }
            years[i] = sdf.format(instance.getTime());
        }
        return years;
    }

    public static String[] generateMonths() {
        String[] months = new String[12];
        for (int i = 0; i < 12; i++) {
            months[i] = (i + 1) + "月";
        }
        return months;
    }

    public static String getTimeByDate(Date date) {//可根据需要自行截取数据显示
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
        return format.format(date);
    }

    /**
     * yyyy-MM-dd
     *
     * @return
     */
    public static String getCurrentDate() {
        Calendar c = Calendar.getInstance();
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        return sdf.format(c.getTime());
    }

    /**
     * yyyy-MM-dd HH:mm:ss
     *
     * @return
     */
    public static String getCurrentDate1() {
        Calendar c = Calendar.getInstance();
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        return sdf.format(c.getTime());
    }

    /**
     * yyyy-MM
     *
     * @return
     */
    public static String getCurrentDate2() {
        Calendar c = Calendar.getInstance();
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM");
        return sdf.format(c.getTime());
    }

    /**
     * yyyy-MM
     *
     * @return
     */
    public static String getCurrentDate3() {
        Calendar c = Calendar.getInstance();
        SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMddHHmmss");
        return sdf.format(c.getTime());
    }

    public static int getDaysOfMonth(int year, int month) {
        Calendar calendar = Calendar.getInstance(Locale.CHINA);
        calendar.set(year, month, 1);
        return calendar.getActualMaximum(Calendar.DAY_OF_MONTH);
    }

    /**
     * HH:mm
     *
     * @return
     */
    public static String getCurrentTime() {
        Calendar c = Calendar.getInstance();
        SimpleDateFormat sdf = new SimpleDateFormat("HH:mm");
        return sdf.format(c.getTime());
    }

    /**
     * 获取系统时间
     *
     * @param type : 1 获取日期 2 获取详细时间
     * @return 日期类型时间
     * @throws ParseException
     */
    public static Date getSystemTime(String type) {
        String Pattern = "yyyy-MM-dd";
        if (type.equals("1")) {
            Pattern = "yyyy-MM-dd";
        }
        if (type.equals("2")) {
            Pattern = "yyyy-MM-dd HH:mm:ss";
        }
        SimpleDateFormat sdf = new SimpleDateFormat(Pattern);
        Calendar calendar = Calendar.getInstance();
        sdf.format(calendar.getTime());
        Date date = null;
        try {
            date = sdf.parse(sdf.format(calendar.getTime()));
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return date;
    }

    /**
     * 获取系统时间
     *
     * @param type : 1 获取日期 2 获取详细时间
     * @return 字符串类型时间
     * @throws ParseException
     */
    public static String getStrSystemTime(String type) {
        String Pattern = "yyyy-MM-dd";
        if (type.equals("1")) {
            Pattern = "yyyy-MM-dd";
        }
        if (type.equals("2")) {
            Pattern = "yyyy-MM-dd HH:mm:ss";
        }
        SimpleDateFormat formatter = new SimpleDateFormat(Pattern);
        Calendar calendar = Calendar.getInstance();
        formatter.format(calendar.getTime());
        String strdate = null;
        strdate = formatter.format(calendar.getTime());
        return strdate;
    }

    /**
     * 获取日期相差天数
     *
     * @param
     * @return 日期类型时间
     * @throws ParseException
     */
    public static Long getDiffDay(String beginDate, String endDate) {
        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
        Long checkday = 0l;
        // 开始结束相差天数
        try {
            checkday = (formatter.parse(endDate).getTime() - formatter.parse(beginDate).getTime())
                    / (1000 * 24 * 60 * 60);
        } catch (ParseException e) {
            e.printStackTrace();
            checkday = null;
        }
        return checkday;
    }

    public static Long getDiffDay(Date beginDate, Date endDate) {
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        String strBeginDate = format.format(beginDate);

        String strEndDate = format.format(endDate);
        return getDiffDay(strBeginDate, strEndDate);
    }

    /**
     * 通过开始时间＋指定相差天数 得到结束时间
     *
     * @param
     * @return 日期类型时间
     * @throws ParseException
     */
    public static Long getDateByBeginDayAndDays(String beginDate, long days) {
        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
        Long endDay = 0l;
        // 开始结束相差天数
        try {
            endDay = formatter.parse(beginDate).getTime() + (1000 * 24 * 60 * 60) * days;
        } catch (ParseException e) {
            e.printStackTrace();
            endDay = null;
        }
        return endDay;
    }

    public static Long getDateByBeginDayAndDays(Date beginDate, long days) {
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        String strBeginDate = format.format(beginDate);

        return getDateByBeginDayAndDays(strBeginDate, days);
    }

    /**
     * 获取日期相月数
     *
     * @param
     * @return 日期类型时间
     * @throws ParseException
     */
    public static int getDiffMonth(String beginDate, String endDate) {
        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
        Date dbeginDate = null;
        Date dendDate = null;
        try {
            dbeginDate = formatter.parse(beginDate);
            dendDate = formatter.parse(endDate);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return getDiffMonth(dbeginDate, dendDate);
    }

    public static int getDiffMonth(Date beginDate, Date endDate) {
        Calendar calbegin = Calendar.getInstance();
        Calendar calend = Calendar.getInstance();
        calbegin.setTime(beginDate);
        calend.setTime(endDate);
        int m_begin = calbegin.get(Calendar.MONTH) + 1; // 获得合同开始日期月份
        int m_end = calend.get(Calendar.MONTH) + 1;
        // 获得合同结束日期月份
        int checkmonth = m_end - m_begin + (calend.get(Calendar.YEAR) - calbegin.get(Calendar.YEAR)) * 12;
        // 获得合同结束日期于开始的相差月份
        return checkmonth;
    }

    public static String getDayOfWeekForChinese(int DayOfWeek) {
        switch (DayOfWeek) {
            case 1:
                return "一";
            case 2:
                return "二";
            case 3:
                return "三";
            case 4:
                return "四";
            case 5:
                return "五";
            case 6:
                return "六";
            case 7:
                return "日";
        }
        return "";
    }

    public static int getSecond(String date, boolean isStart) {
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
        Date selDate = null;
        try {
            selDate = format.parse(date);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        if (selDate == null) {
            return 0;
        }
        if (isStart)
            return (int) (selDate.getTime() / 1000);
        else
            return (int) ((selDate.getTime() / 1000) + 86399);
    }

    public static int getSecond(Date date, boolean isStart) {
        if (date == null) {
            return 0;
        }
        if (isStart)
            return (int) (date.getTime() / 1000);
        else
            return (int) ((date.getTime() / 1000) + 86399);
    }


    /**
     * 将时间戳转为日期
     *
     * @param timestamp
     * @return
     */
    public static String getTime(int timestamp) {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        String time = null;
        try {
            String str = sdf.format(new Timestamp(intToLong(timestamp)));
            time = str;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return time;
    }

    // java Timestamp构造函数需传入Long型
    public static long intToLong(int i) {
        long result = (long) i;
        result *= 1000;
        return result;
    }


    public static String getDateStringByDateString(String dateString) {
        SimpleDateFormat format = new SimpleDateFormat("yyyyMMddHHmmss");
        Date date = null;
        String str = dateString;
        try {
            date = format.parse(str);
            SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            return formatter.format(date);

        } catch (ParseException e) {
            e.printStackTrace();
        }

        return getCurrentDate1();
    }

    public static String getDateStringByDateString2(String dateString) {
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        Date date = null;
        String str = dateString;
        try {
            date = format.parse(str);
            SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
            return formatter.format(date);

        } catch (ParseException e) {
            e.printStackTrace();
        }

        return getCurrentDate1();
    }

    public static String getDateStringByDateString3(String dateString) {
        if (dateString == null || dateString.equals(""))
            return null;
        return dateString;
    }

    public static String getDateStringEnd(String dateString) {
        if (dateString == null || dateString.equals(""))
            return null;
        else
            return dateString + " 23:59:59";
    }

    public static String getDateStringByDateString4(String dateString) {
        SimpleDateFormat format = new SimpleDateFormat("yyyyMMdd");
        Date date = null;
        String str = dateString;
        try {
            date = format.parse(str);
            SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
            return formatter.format(date);

        } catch (ParseException e) {
            e.printStackTrace();
        }

        return "";
    }


    //判断时间date1是否在时间date2之前
    //时间格式 2005-4-21 16:16:34

    /**
     * 判断某时间是否在某时间之前
     *
     * @param dateStr1
     * @param dateStr2
     * @return
     */
    public static boolean isDateBefore(String dateStr1, String dateStr2) {
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        Date date1 = null;
        Date date2 = null;
        String str1 = dateStr1;
        String str2 = dateStr2;
        try {
            date1 = format.parse(str1);
            date2 = format.parse(str2);
            return date1.before(date2);
        } catch (ParseException e) {
            return false;
        }
    }
}