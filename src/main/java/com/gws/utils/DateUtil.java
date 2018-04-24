
package com.gws.utils;


import org.apache.commons.lang3.StringUtils;
import org.ocpsoft.prettytime.PrettyTime;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

/**
 *
 */
public class DateUtil {

    /**
     * 一天的秒数
     */
    public static final int DAY_SECOND = 86400;
    
    public static final String FORMAT_YYYYMMDD = "yyyyMMdd";
    public static final String FORMAT_YYYYMMDDHHMMSS = "yyyyMMddhhmmss";
    
    public static final String FORMAT_YYYY_MM_DD_HH_MM_SS = "yyyy-MM-dd HH:mm:ss";

    public static final String FORMAT_YYYY_MM_DD = "yyyy-MM-dd";
    
    public static final int ONE_SECOND = 1000;
	public static final int ONE_MINUTE = 60 * ONE_SECOND;
	public static final int ONE_HOUR = 60 * ONE_MINUTE;
	public static final long ONE_DAY = 24 * ONE_HOUR;
    
    public static String getFormatDate(Date date, String format) {
        SimpleDateFormat sdf = new SimpleDateFormat(format);
        return sdf.format(date);
    }

    public static String getCurrectData() {
        return getFormatDate(new Date(), "yyyy-MM-dd HH:mm:ss");
    }

    public static String getCurrectDay() {
        return getFormatDate(new Date(), "yyyy-MM-dd");
    }

    public static String getFormatDate(Date date) {
        return getFormatDate(date, "yyyy-MM-dd HH:mm:ss");
    }

    public static int currentSecond() {
        return Long.valueOf(System.currentTimeMillis() / 1000).intValue();
    }

    public static int getDateSecond(Date date) {
        return Long.valueOf((date.getTime() / 1000)).intValue();
    }

    public static String formatDate(Integer unixSecond) {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        Date date = new Date();
        if (unixSecond != null) {
            date.setTime(unixSecond.longValue() * 1000L);
        }
        return sdf.format(date);
    }
    
    public static String formatDate(Integer unixSecond,String format){
    	 SimpleDateFormat sdf = new SimpleDateFormat(format);
         Date date = new Date();
         if (unixSecond != null) {
             date.setTime(unixSecond.longValue() * 1000L);
         }
         return sdf.format(date);
    }

    public static Date formatInt2Date(Integer unixSecond) {
        Date date = new Date();
        if (unixSecond != null) {
            date.setTime(unixSecond.longValue() * 1000L);
        }
        return date;
    }

    /**
     * @param str
     * @desc 字符串 yyyy-MM-dd hh:mm:ss 转换为 integer类型的时间戳
     * @returnSimpleDateFormat
     */
    public static Integer string2Timestamp(String str) {
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        try {
            Date date = format.parse(str);
            Long dt = date.getTime() / 1000;
            return Integer.parseInt(dt.toString());
        } catch (Exception e) {

        }
        return null;
    }

    /**
     * @param str
     * @return
     */
    public static Integer hourMinuteString2Timestamp(String str) {
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm");
        try {
            Date date = format.parse(str);
            Long dt = date.getTime() / 1000;
            return Integer.parseInt(dt.toString());
        } catch (Exception e) {
        }
        return null;
    }

    /**
     * @param str
     * @desc 字符串 hh:mm 转换为 当天integer类型的时间戳
     * @returnSimpleDateFormat
     */
    public static Integer stringHourMinu2Timestamp(String str) {
        String today = getFormatDate(new Date(), "yyyy-MM-dd");
        String todayHourMinu = today + " " + str + ":00";
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        try {
            Date date = format.parse(todayHourMinu);
            Long dt = date.getTime() / 1000;
            return Integer.parseInt(dt.toString());
        } catch (Exception e) {
        }
        return null;
    }

    /**
     * @param str
     * @desc 字符串 yyyy-MM-dd hh 转换为 integer类型的时间戳
     * @returnSimpleDateFormat
     */
    public static Integer hourString2Timestamp(String str) {
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH");
        try {
            Date date = format.parse(str);
            Long dt = date.getTime() / 1000;
            return Integer.parseInt(dt.toString());
        } catch (Exception e) {

        }
        return null;
    }

    /**
     * 获取当前天long值
     *
     * @return
     * @throws ParseException
     */
    public static Integer getCurrentDay() {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd 00:00:00");
        Calendar cd = Calendar.getInstance();
        String strCurrentDay = sdf.format(cd.getTime());
        // long currentTime = sdf.parse(strCurrentDay).getTime();
        // 不能调用注释代码生成标准时间戳,13位,
        Integer currentTime = string2Timestamp(strCurrentDay);
        return currentTime;
    }

    /**
     * 获取第二天凌晨时间
     *
     * @return
     * @throws ParseException
     */
    public static Integer getCurrentNextDay() throws ParseException {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd 00:00:00");
        Calendar cd = Calendar.getInstance();
        cd.setTime(cd.getTime());
        cd.add(Calendar.HOUR_OF_DAY, 24);
        String strCurrentDay = sdf.format(cd.getTime());
        // 不能调用注释代码生成标准时间戳,13位,
        Integer currentTime = string2Timestamp(strCurrentDay);
        return currentTime;
    }

    /**
     * 获取当前的时间戳，格式为yyyyMMddhhmmssSSS
     *
     * @return
     */
    public static String getCurrentTimeStamp() {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMddhhmmssSSS");
        Calendar calendar = Calendar.getInstance();
        return sdf.format(calendar.getTime());
    }

    /**
     * 获取当前的时间戳，格式为yyyyMMddhhmmss
     *
     * @return
     */
    public static String getCurrentTimeStampNoMill() {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMddhhmmss");
        Calendar calendar = Calendar.getInstance();
        return sdf.format(calendar.getTime());
    }

    /**
     * @param str
     * @return
     * @desc 字符串 yyyy-MM-dd hh 转换为 integer类型的时间戳
     */
    public static Integer stringHour2Timestamp(String str) {
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH");
        try {
            Date date = format.parse(str);
            Long dt = date.getTime() / 1000;
            return Integer.parseInt(dt.toString());
        } catch (Exception e) {

        }
        return null;
    }

    public static Integer stringMonth2Timestamp(String str) {
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM");
        try {
            Date date = format.parse(str);
            Long dt = date.getTime() / 1000;
            return Integer.parseInt(dt.toString());
        } catch (Exception e) {

        }
        return null;
    }

    /**
     * @param timestamp
     * @return
     * @desc 时间戳转换为时间字符串
     */
    public static String timestamp2String(Integer timestamp) {

        if (null == timestamp) {
            return null;
        }

        Date date = new Date();
        if (timestamp != null) {
            date.setTime(timestamp.longValue() * 1000L);
        }
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        String dateStr = format.format(date);
        return dateStr;
    }

    public static String formatTimeStamp(Integer timestamp, String formatStr) {
        if (StringUtils.isEmpty(formatStr)) {
            formatStr = "yyyy-MM-dd HH:mm:ss";
        }
        if (null == timestamp) {
            return null;
        }

        Date date = new Date();
        if (timestamp != null) {
            date.setTime(timestamp.longValue() * 1000L);
        }
        SimpleDateFormat format = new SimpleDateFormat(formatStr);
        String dateStr = format.format(date);
        return dateStr;
    }

    public static boolean isNowYear(Integer timestamp) {
        Integer year = Calendar.getInstance().get(Calendar.YEAR);
        if (timestamp2String(timestamp).startsWith(year.toString())) {
            return true;
        }
        return false;
    }

    /**
     * 时间格式yyyy-MM
     *
     * @param str
     * @return
     */
    public static Integer nextMonth(String str) {

        Integer current = stringMonth2Timestamp(str);
        Calendar cal = Calendar.getInstance();
        cal.setTimeInMillis(current * 1000L);
        cal.set(Calendar.MONTH, cal.get(Calendar.MONTH) + 1);
        Long nextMonth = cal.getTimeInMillis() / 1000;
        return Integer.valueOf(nextMonth.toString());
    }

    public static Integer getYear() {
        Calendar cal = Calendar.getInstance();
        return cal.get(Calendar.YEAR);
    }

    public static Integer getMonth() {
        Calendar cal = Calendar.getInstance();
        return cal.get(Calendar.MONTH) + 1;
    }

    public static Integer getDay() {
        Calendar cal = Calendar.getInstance();
        return cal.get(Calendar.DATE);
    }

    public static Integer getHour() {
        Calendar cal = Calendar.getInstance();
        return cal.get(Calendar.HOUR_OF_DAY);
    }

    /**
     * 判断是否为闰年
     *
     * @param year
     * @return
     */
    public static boolean checkIsLeapYear(int year) {
        if ((year % 4 == 0) && ((year % 100 != 0) | (year % 400 == 0))) {
            return true;
        } else {
            return false;
        }
    }

    /**
     * 判断是否为闰年
     */
    public static boolean checkIsLeapYear() {
        return checkIsLeapYear(DateUtil.getYear());
    }

    /**
     * 获取上个月数字：yyyyMM（201307）。
     *
     * @return
     */
    public static Integer getLastMonthNum() {
        Calendar calendar = Calendar.getInstance();
        calendar.add(Calendar.MONTH, -1);
        SimpleDateFormat format = new SimpleDateFormat("yyyyMM");
        return Integer.valueOf(format.format(calendar.getTime()));
    }

    /**
     * 获取该周期的上一个周期
     *
     * @param interval
     * @return
     */
    public static Integer getLastMonthNum(Integer interval) {
        validateYearMonthInterval(interval);
        int year = getYearInInterval(interval);
        int month = getMonthInInterval(interval);
        Calendar calendar = Calendar.getInstance();
        calendar.set(year, month - 1, 1);
        calendar.add(Calendar.MONTH, -1);
        SimpleDateFormat format = new SimpleDateFormat("yyyyMM");
        return Integer.valueOf(format.format(calendar.getTime()));
    }

    /**
     * 获取该区间的第一秒
     *
     * @param interval yyyyMM(201311)
     * @return
     */
    public static Integer getFirstSecondInInterval(Integer interval) {
        validateYearMonthInterval(interval);
        int year = getYearInInterval(interval);
        int month = getMonthInInterval(interval);
        Calendar calendar = Calendar.getInstance();
        calendar.set(year, month - 1, 1);
        calendar.set(Calendar.HOUR_OF_DAY, 0);
        calendar.set(Calendar.MINUTE, 0);
        calendar.set(Calendar.SECOND, 0);
        return convertMillisToUnixTime(calendar.getTimeInMillis());
    }

    /**
     * 获取上个月最后一秒
     *
     * @return
     */
    public static Integer getLastSecondInInterval(Integer interval) {
        validateYearMonthInterval(interval);
        int year = getYearInInterval(interval);
        int month = getMonthInInterval(interval);
        Calendar calendar = Calendar.getInstance();
        calendar.set(year, month - 1 + 1, 1);
        calendar.add(Calendar.DAY_OF_MONTH, -1);
        calendar.set(Calendar.HOUR_OF_DAY, 23);
        calendar.set(Calendar.MINUTE, 59);
        calendar.set(Calendar.SECOND, 59);
        return convertMillisToUnixTime(calendar.getTimeInMillis());
    }

    private static void validateYearMonthInterval(Integer interval) {
        if (interval == null || interval == 0) {
            throw new IllegalArgumentException("Interval is null or 0.");
        }
        if (interval % 100 > 12 || interval % 100 < 1) {
            throw new IllegalArgumentException("Interval month part is not in [1, 12].");
        }
        if (interval / 100 <= 0) {
            throw new IllegalArgumentException("Interval year part is missing.");
        }
    }

    private static int getYearInInterval(Integer interval) {
        return interval / 100;
    }

    private static int getMonthInInterval(Integer interval) {
        return interval % 100;
    }

    private static Integer convertMillisToUnixTime(Long millis) {
        if (millis == null) {
            return null;
        }
        return Long.valueOf((millis / 1000)).intValue();
    }

    /**
     * 转换Date到UnixtTime
     *
     * @param date
     * @return
     */
    public static Integer convertDateToUnixTime(Date date) {
        if (date == null) {
            return null;
        }
        return Long.valueOf(date.getTime() / 1000).intValue();
    }

    /**
     * 获取昨天的第一秒
     *
     * @return
     */
    public static Integer getYesterdayBegin() {
        Calendar calendar = Calendar.getInstance();
        calendar.add(Calendar.DATE, -1);
        calendar.set(Calendar.HOUR_OF_DAY, 0);
        calendar.set(Calendar.MINUTE, 0);
        calendar.set(Calendar.SECOND, 0);
        return convertMillisToUnixTime(calendar.getTimeInMillis());
    }

    /**
     * 获取昨天的最后一秒
     *
     * @return
     */
    public static Integer getYesterDayEnd() {
        Calendar calendar = Calendar.getInstance();
        calendar.add(Calendar.DATE, -1);
        calendar.set(Calendar.HOUR_OF_DAY, 23);
        calendar.set(Calendar.MINUTE, 59);
        calendar.set(Calendar.SECOND, 59);
        return convertMillisToUnixTime(calendar.getTimeInMillis());
    }

    /**
     * 获取昨天日期:yyyy-MM-dd
     *
     * @return
     */
    public static String getYesterdayString() {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        Calendar calendar = Calendar.getInstance();
        calendar.add(Calendar.DATE, -1);
        return sdf.format(calendar.getTime());
    }

	/*public static void main(String[] args) {
		// 1365436800
		System.out.println("2014-01-13 00:00:00:"
				+ DateUtil.string2Timestamp("2014-01-16 19:30:00"));
		System.out.println("2014-01-30 00:00:00:"
				+ DateUtil.string2Timestamp("2014-01-30 00:00:00"));
		System.out.println("2014-02-01 00:00:00:"
				+ DateUtil.string2Timestamp("2014-02-01 00:00:00"));
		System.out.println("2014-02-04 00:00:00:"
				+ DateUtil.string2Timestamp("2014-02-04 00:00:00"));
		System.out.println("now:" + DateUtil.currentSecond());
		
		
		System.out.println(DateUtil.formatDate(DateUtil
				.string2Timestamp("2013-07-12 00:00:00")));
		System.out.println("2013-07-22:"
				+ DateUtil.string2Timestamp("2013-07-22 00:00:00"));
		System.out.println(DateUtil.string2Timestamp("2013-07-23 00:10:03")
				- DateUtil.string2Timestamp("2013-07-22 00:00:00"));
	}*/

    /**
     * 取得当前月的第1天的第1秒
     *
     * @return
     */
    public static Integer getCurrentMonthBegin() {
        return getMonthBegin(0);
    }

    /**
     * 取得当某个月的第1天的第1秒，以当前月为标准，add=0就表示取得当个月的第1天第1秒
     *
     * @param amount
     * @return
     */
    public static Integer getMonthBegin(int amount) {
        Calendar calendar = Calendar.getInstance();
        calendar.set(Calendar.DAY_OF_MONTH, 1);
        calendar.set(Calendar.HOUR_OF_DAY, 0);
        calendar.set(Calendar.MINUTE, 0);
        calendar.set(Calendar.SECOND, 0);
        calendar.add(Calendar.MONTH, amount);
        return convertMillisToUnixTime(calendar.getTimeInMillis());
    }

    public static Integer getMonthBegin(Date date) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        calendar.set(Calendar.DAY_OF_MONTH, 1);
        calendar.set(Calendar.HOUR_OF_DAY, 0);
        calendar.set(Calendar.MINUTE, 0);
        calendar.set(Calendar.SECOND, 0);
        return convertMillisToUnixTime(calendar.getTimeInMillis());
    }

    /**
     * 取得某个日期的月份的最后一秒（比如那个月份只有30天，就返回30号23时59分59秒）
     *
     * @param date
     * @return
     */
    public static Integer getMonthEnd(Date date) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        calendar.add(Calendar.MONTH, 1);
        calendar.set(Calendar.DAY_OF_MONTH, 1);
        calendar.set(Calendar.HOUR_OF_DAY, 0);
        calendar.set(Calendar.MINUTE, 0);
        calendar.set(Calendar.SECOND, 0);
        return convertMillisToUnixTime(calendar.getTimeInMillis()) - 1;
    }

    /**
     * @param amount
     * @return
     */
    public static Integer getDayBegin(int amount) {
        Calendar calendar = Calendar.getInstance();
        calendar.add(Calendar.DATE, amount);
        calendar.set(Calendar.HOUR_OF_DAY, 0);
        calendar.set(Calendar.MINUTE, 0);
        calendar.set(Calendar.SECOND, 0);
        return convertMillisToUnixTime(calendar.getTimeInMillis());
    }

    /**
     * @param date
     * @return
     */
    public static Integer getDayBegin(Date date) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        calendar.set(Calendar.HOUR_OF_DAY, 0);
        calendar.set(Calendar.MINUTE, 0);
        calendar.set(Calendar.SECOND, 0);
        return convertMillisToUnixTime(calendar.getTimeInMillis());
    }

    /**
     * 判断给出的时间是不是当前月
     *
     * @param time
     * @return
     */
    public static boolean isNowMonth(Integer time) {
        Calendar calendar = Calendar.getInstance();
        Integer nowMonth = calendar.get(Calendar.MONTH);
        Integer newYear = calendar.get(Calendar.YEAR);
        calendar.setTimeInMillis(time * 1000L);
        return nowMonth.equals(calendar.get(Calendar.MONTH))
                && newYear.equals(calendar.get(Calendar.YEAR));
    }

    public static String currentDay() {
        return DateUtil.getFormatDate(new Date(), "yyyyMMdd");
    }

    /**
     * 获取相隔时间日期str:
     *
     * @param delta
     * @param dateFormat , default : "yyyy-MM-dd"
     * @return
     */
    public static String getDeltaDayString(int delta, String dateFormat) {
        if (StringUtils.isEmpty(dateFormat)) {
            dateFormat = "yyyy-MM-dd";
        }
        SimpleDateFormat sdf = new SimpleDateFormat(dateFormat);
        Calendar calendar = Calendar.getInstance();
        calendar.add(Calendar.DATE, delta);
        return sdf.format(calendar.getTime());
    }

    public static Integer getHourInterval(int interval) {
        Calendar c = Calendar.getInstance();
        c.add(Calendar.DATE, interval); //日期加1天
        return convertMillisToUnixTime(c.getTimeInMillis());
    }


    /**
     * 等同于getTheDayBegin(null, 0)
     *
     * @return
     */
    public static Date getCurrentDayBegin() {
        Long timeInMills = 0L;
        try {
            Calendar start = Calendar.getInstance();
            start.set(Calendar.HOUR_OF_DAY, 0);
            start.set(Calendar.MINUTE, 0);
            start.set(Calendar.SECOND, 0);
            timeInMills = start.getTimeInMillis();
        } catch (Exception e) {
            e.printStackTrace();
        }
        Date date = new Date(timeInMills);
        return date;
    }

    /**
     * 等同于getTheDayEnd(null, 0)
     *
     * @return
     */
    public static Date getCurrentDayEnd() {
        Long timeInMills = 0L;
        try {
            Calendar start = Calendar.getInstance();
            start.set(Calendar.HOUR_OF_DAY, 23);
            start.set(Calendar.MINUTE, 59);
            start.set(Calendar.SECOND, 59);
            timeInMills = start.getTimeInMillis();
        } catch (Exception e) {
            e.printStackTrace();
        }
        Date date = new Date(timeInMills);
        return date;
    }

    /**
     * 等同于getTheDayBegin(theDate, 0)
     *
     * @param theDate
     * @return
     */
    public static Date getTheDayBegin(Date theDate) {
        Long timeInMills = 0L;
        try {
            Calendar start = Calendar.getInstance();
            start.setTime(theDate);
            start.set(Calendar.HOUR_OF_DAY, 0);
            start.set(Calendar.MINUTE, 0);
            start.set(Calendar.SECOND, 0);
            timeInMills = start.getTimeInMillis();
        } catch (Exception e) {
            e.printStackTrace();
        }
        Date date = new Date(timeInMills);
        return date;
    }

    /**
     * @param theDate
     * @param pos
     * @return
     */
    public static Date getTheDayBegin(Date theDate, int pos) {
        Long timeInMills = 0L;
        try {
            Calendar start = Calendar.getInstance();
            if (null != theDate) {
                start.setTime(theDate);
            }
            start.add(Calendar.DAY_OF_MONTH, pos);
            start.set(Calendar.HOUR_OF_DAY, 0);
            start.set(Calendar.MINUTE, 0);
            start.set(Calendar.SECOND, 0);
            timeInMills = start.getTimeInMillis();
        } catch (Exception e) {
            e.printStackTrace();
        }
        Date date = new Date(timeInMills);
        return date;
    }

    /**
     * @param theDate
     * @return
     */
    public static Date getTheDayEnd(Date theDate, int pos) {
        Long timeInMills = 0L;
        try {
            Calendar start = Calendar.getInstance();
            if (null != theDate) {
                start.setTime(theDate);
            }
            start.add(Calendar.DAY_OF_MONTH, pos);
            start.set(Calendar.HOUR_OF_DAY, 23);
            start.set(Calendar.MINUTE, 59);
            start.set(Calendar.SECOND, 59);
            timeInMills = start.getTimeInMillis();
        } catch (Exception e) {
            e.printStackTrace();
        }
        Date date = new Date(timeInMills);
        return date;
    }


    /**
     * @param str
     * @desc 字符串 yyyy-MM-dd 转换为 integer类型的时间戳
     * @returnSimpleDateFormat
     */
    public static Integer dateString2Timestamp(String str) {
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
        try {
            Date date = format.parse(str);
            Long dt = date.getTime() / 1000;
            return Integer.parseInt(dt.toString());
        } catch (Exception e) {

        }
        return null;
    }
    
    public static String convertDateToString(String dateFormat,Date date){
    	  if (StringUtils.isEmpty(dateFormat)) {
              dateFormat = "yyyy-MM-dd";
          }
          SimpleDateFormat sdf = new SimpleDateFormat(dateFormat);
          if(date==null){
        	  date = new Date();
          }
          return sdf.format(date);
    }
    
	public static String timestampToDates(int time,String formatPatton) {
		SimpleDateFormat format = new SimpleDateFormat(formatPatton);
		Date date = new Date(time * 1000L);
		String dateStr = format.format(date);
		return dateStr;
	}
	
	
	/**
	 * 
	 * 【将字符串转为unix时间戳 按照指定格式】
	 * 
	 * @author wangdong 2016年4月12日
	 * @param userTime
	 * @param format
	 * @return
	 */
	public static int getTimeFormat(String userTime, String format) {
		int reTime = 0;
		SimpleDateFormat sdf = new SimpleDateFormat(format);
		Date d;
		try {
			d = sdf.parse(userTime);
			long l = d.getTime();
			String str = String.valueOf(l);
			reTime = Integer.parseInt(str.substring(0, 10));
		} catch (ParseException e) {
			e.printStackTrace();
		}
		return reTime;
	}
	

    /**
     * 指定日期 的指定月份之后或之前的日期
     * @param date
     * @param i
     * @param formate
     * @return
     */
    public static String getDateDelayMonth(Date date,int i,String formate){
        Calendar c = Calendar.getInstance();
        c.setTime(date);
        c.add(Calendar.MONTH, i);
        SimpleDateFormat sdf = new SimpleDateFormat(formate);
        String str = sdf.format(c.getTime());
        return str;
    }

    public static Date parse(String s, String fmt) {
        SimpleDateFormat sdf = new SimpleDateFormat(fmt);
        try {
            return sdf.parse(s);
        } catch (ParseException e) {
        }
        return null;
    }

    public static String prettyTime(Integer time) {
        Date date = new Date(time*1000l);
        PrettyTime prettyTime = new PrettyTime(new Date());
        prettyTime.setLocale(Locale.CHINESE);
        return prettyTime.format(date).replace(" ", "");
    }

    public static void main(String[] args) {
        System.out.print(DateUtil.prettyTime(1495876635));

    }

    /**
     * 获取当前时间时间戳
     * @return
     */
    public static Integer getUnixTimestamp(){
        Date date = new Date();
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        String time = dateFormat.format(date);
        Integer unixTimestamp = string2Timestamp(time);
        return unixTimestamp;
    }

    /**
     * 获取当天0点时间戳
     * @return
     */
    public static Integer getTimemorning(){
        Calendar cal = Calendar.getInstance();
        cal.set(Calendar.HOUR_OF_DAY, 0);
        cal.set(Calendar.SECOND, 0);
        cal.set(Calendar.MINUTE, 0);
        cal.set(Calendar.MILLISECOND, 0);
        return (int)(cal.getTimeInMillis()/1000);
    }

    /**
     * 获取当天23点59分59秒的时间戳
     * @return
     */
    public static Integer getTimeEvening(){
        Calendar cal = Calendar.getInstance();
        cal.set(Calendar.HOUR_OF_DAY, 23);
        cal.set(Calendar.SECOND, 59);
        cal.set(Calendar.MINUTE, 59);
        cal.set(Calendar.MILLISECOND, 0);
        return (int)(cal.getTimeInMillis()/1000);
    }
}
