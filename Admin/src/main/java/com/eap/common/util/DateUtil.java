package com.eap.common.util;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.sql.Timestamp;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.Locale;

/**
 * 
 * <strong>Title : DateUtil </strong>. <br>
 * <strong>Description : 日期工具类</strong> <br>
 * <strong>Create on : 2009-12-22 下午04:53:51 </strong>. <br>
 * <p>
 * <strong>Copyright (C) Labsoft Pte,Ltd.</strong> <br>
 * </p>
 * 
 * @author CharlesXi<br>
 * @version <strong>rsLab v 1.0.0</strong> <br>
 *          <br>
 *          <strong>修改历史: .</strong> <br>
 *          修改人� 修改日期 修改描述<br>
 *          -------------------------------------------<br>
 *          <br>
 *          <br>
 */
public class DateUtil {
	public final static String YYYY = "yyyy";
	public final static String MM = "MM";
	public final static String DD = "dd";
	public final static String YYYY_MM_DD = "yyyy-MM-dd";
	public final static String YYYY_MM = "yyyy-MM";
	public final static String HH_MM_SS = "HH:mm:ss";
	public final static String YYYY_MM_DD_HH_MM_SS = "yyyy-MM-dd HH:mm:ss";

	public static String formatStr_yyyyMMddHHmmssS = "yyyy-MM-dd HH:mm:ss.S";
	public static String formatStr_yyyyMMddHHmmss = "yyyy-MM-dd HH:mm:ss";
	public static String formatStr_yyyyMMddHHmm = "yyyy-MM-dd HH:mm";
	public static String formatStr_yyyyMMddHH = "yyyy-MM-dd HH";
	public static String formatStr_yyyyMMdd = "yyyy-MM-dd";
	public static String formatStr_yyyy = "yyyy";
	public static String[] formatStr = { formatStr_yyyyMMddHHmmss,
			formatStr_yyyyMMddHHmm, formatStr_yyyyMMddHH, formatStr_yyyyMMdd ,formatStr_yyyy};

	public static String begin = "";
	public static String end = "";
	public static String now = new java.sql.Date(new Date().getTime())
			.toString();

	private static final int DAY_MILLISECOND = 86400000;

	/**
	 * 
	 * 日期格式化－将<code>Date</code>类型的日期格式化为<code>String</code>型
	 * 
	 * @param date
	 *            日期
	 * @param pattern
	 *            格式化字串
	 * @return 返回类型 String 日期字符串
	 */
	public static String format(Date date, String pattern) {
		if (date == null)
			return "";
		else
			return getFormatter(pattern).format(date);
	}
	
	
	  public static Object getLastMilliSecond(Object date) throws SecurityException, NoSuchMethodException, IllegalArgumentException, InstantiationException, IllegalAccessException, InvocationTargetException
	  {
	    Calendar cal = Calendar.getInstance();
	    cal.setTime((Date)date);
	    cal.add(5, 1);
	    cal.add(14, -1);
	    Constructor constructor = date.getClass().getConstructor(new Class[] { Long.TYPE });
	    Object newDate = constructor.newInstance(new Object[] { Long.valueOf(cal.getTimeInMillis()) });
	    return newDate;
	  }

	/**
	 * 
	 * 默认把日期格式化成yyyy-MM-dd格式
	 * 
	 * @param date
	 *            被格式化的时间
	 * @return 返回类型 String 日期字符串
	 */
	public static String format(Date date) {
		if (date == null)
			return "";
		else
			return getFormatter(YYYY_MM_DD).format(date);
	}

	/**
	 * 
	 * 日期解析－将<code>String<	类型的日期解析为<code>Date</code>型
	 * @param strDate  日期字串
	 * @param pattern  格式化字串
	 * @throws ParseException  
	 * @return 返回类型 Date  一个被格式化了的<code>Date</code>日期
	 */
	public static Date parse(String strDate, String pattern)
			throws ParseException {
		try {
			return getFormatter(pattern).parse(strDate);
		} catch (ParseException pe) {
			throw new ParseException(
					"Method parse in Class DateUtil  err: parse strDate fail.",
					pe.getErrorOffset());
		}
	}

	/**
	 * 
	 * 获取当前日期
	 * 
	 * @return 返回类型 Date 返回类型 一个包含年月日的<code>Date</code>型日期
	 */
	public static synchronized Date getCurrDate() {
		Calendar calendar = Calendar.getInstance();
		return calendar.getTime();
	}

	/**
	 * 
	 * 将日期类型转换为simpleDateFormat类型
	 * 
	 * @param pattern
	 *            要转换的日期类型
	 * @return 返回类型 SimpleDateFormat 返回一个SimpleDateFormat类型的日期字符串
	 */
	private static SimpleDateFormat getFormatter(String pattern) {
		return new SimpleDateFormat(pattern);
	}

	/**
	 * 
	 * 获取当前日期
	 * 
	 * @return 返回类型 String 一个包含年月日的<code>String</code>型日期，但不包含时分秒。yyyy-MM-dd
	 */
	public static String getCurrDateStr(String separator) {
		return format(getCurrDate(), convertFormat(YYYY_MM_DD, separator));
	}

	/**
	 * 
	 * 获取当前时间
	 * 
	 * @return 返回类型 String 一个包含年月日时分秒的<code>String</code>型日期。hh:mm:ss
	 */
	public static String getCurrTimeStr(String separator) {
		return format(getCurrDate(), convertFormat(HH_MM_SS, separator));
	}

	/**
	 * 
	 * 获取当前完整时间
	 * 
	 * @return 返回类型 String 一个包含年月日时分秒的<code>String</code>型日期。yyyy-MM-dd
	 *         hh:mm:ss
	 */
	public static String getCurrDateTimeStr(String separator) {
		return format(getCurrDate(), convertFormat(YYYY_MM_DD_HH_MM_SS, separator));
	}

	public static String convertFormat(String formatStr, String separator) {
		return formatStr != null && separator != null ? formatStr.replaceAll("-", separator) : formatStr;
	}

	/**
	 * 
	 * 获取当前年份样式
	 * 
	 * @return 返回类型 String 当前年分 yyyy
	 */
	public static String getYear() {
		return format(getCurrDate(), YYYY);
	}

	/**
	 * 
	 * 获取当前季度的第一个月
	 * 
	 * @param quarter
	 *            第几季度:如 1,2,3,4
	 * @return 返回类型 String 当前季度的第一个月数
	 */
	public static String getFirstMonthOfQuarter(String quarter) {
		int qu = Integer.parseInt(quarter);
		String month = "";
		if (qu == 1) {
			month = "01";
		} else if (qu == 2) {
			month = "04";
		} else if (qu == 3) {
			month = "07";
		} else if (qu == 4) {
			month = "10";
		}
		return month;
	}

	/**
	 * 
	 * 获取当前季度的最后一个月
	 * 
	 * @param quarter
	 *            第几季度 如 1,2,3,4
	 * @return 返回类型 String 当前季度的最后一个月
	 */
	public static String getLastMonthOfQuarter(String quarter) {
		int qu = Integer.parseInt(quarter);
		String month = "";
		if (qu == 1) {
			month = "03";
		} else if (qu == 2) {
			month = "06";
		} else if (qu == 3) {
			month = "09";
		} else if (qu == 4) {
			month = "12";
		}
		return month;
	}

	/**
	 * 
	 * 获取当前季度
	 * 
	 * @return 返回类型 String 当前季度 如1,2,3,4
	 */
	public static String getQuarter() {
		String quar = "";
		if (Integer.parseInt(getMonth()) == 1
				|| Integer.parseInt(getMonth()) == 2
				|| Integer.parseInt(getMonth()) == 3) {
			quar = "1";
		} else if (Integer.parseInt(getMonth()) == 4
				|| Integer.parseInt(getMonth()) == 5
				|| Integer.parseInt(getMonth()) == 6) {
			quar = "2";
		} else if (Integer.parseInt(getMonth()) == 7
				|| Integer.parseInt(getMonth()) == 8
				|| Integer.parseInt(getMonth()) == 9) {
			quar = "3";
		} else if (Integer.parseInt(getMonth()) == 10
				|| Integer.parseInt(getMonth()) == 11
				|| Integer.parseInt(getMonth()) == 12) {
			quar = "4";
		}
		return quar;
	}

	/**
	 * 
	 * 获取当前月
	 * 
	 * @return 返回类型 String 获取当前月
	 */
	public static String getMonth() {
		return format(getCurrDate(), MM);
	}

	/**
	 * 
	 * 获取当前周
	 * 
	 * @return 返回类型 String 获取当前周
	 */
	public static String getWeek() {
		Calendar cal = Calendar.getInstance();
		int weekofmonth = cal.get(Calendar.DAY_OF_WEEK_IN_MONTH);
		return String.valueOf(weekofmonth);
	}

	/**
	 * 
	 * 根据输入的年月周数来取该周第一天
	 * 
	 * @param year
	 *            年份(> 0)
	 * @param month
	 *            月份(1-12)
	 * @param week
	 *            当月周数(1-5)
	 * @return 返回类型 String 该周第一天（周日）
	 */
	public static String getFirstDayByMonthWeek(int year, int month, int week) {
		DateFormat df = new SimpleDateFormat("yyyy-MM-dd ");
		Calendar c = Calendar.getInstance();
		c.set(Calendar.YEAR, year);
		c.set(Calendar.MONTH, month - 1);
		c.set(Calendar.WEEK_OF_MONTH, week);

		int firstDayofweek = c.getFirstDayOfWeek();

		Calendar c1 = Calendar.getInstance();
		c1.set(Calendar.YEAR, year);
		c1.set(Calendar.MONTH, month - 1);
		c1.set(Calendar.WEEK_OF_MONTH, week);
		c1.set(Calendar.DAY_OF_WEEK, firstDayofweek);
		Date d1 = new Date(c1.getTimeInMillis());
		return df.format(d1);
	}

	/**
	 * 根据输入的年月周数来取该周最后一天
	 * 
	 * @param year
	 *            年份(> 0)
	 * @param month
	 *            月份(1-12)
	 * @param week
	 *            当月周数(1-5)
	 * @return 返回类型 String 该周最后一天（周六）
	 */
	public static String getLastDayByMonthWeek(int year, int month, int week) {
		DateFormat df = new SimpleDateFormat("yyyy-MM-dd ");
		Calendar c = Calendar.getInstance();
		c.set(Calendar.YEAR, year);
		c.set(Calendar.MONTH, month - 1);
		c.set(Calendar.WEEK_OF_MONTH, week);

		int firstDayofweek = c.getFirstDayOfWeek();
		int lastDayofweek = firstDayofweek + 6;

		Calendar c2 = Calendar.getInstance();
		c2.set(Calendar.YEAR, year);
		c2.set(Calendar.MONTH, month - 1);
		c2.set(Calendar.WEEK_OF_MONTH, week);
		c2.set(Calendar.DAY_OF_WEEK, lastDayofweek);
		Date d2 = new Date(c2.getTimeInMillis());
		return df.format(d2);
	}

	/**
	 * 计算两个日期之间的月差
	 * 
	 * @param strDate1
	 *            日期字串1
	 * @param strDate2
	 *            日期字串2
	 * @return 返回类型 int 两个日期之间的月差
	 */
	public static int getIntevalMonth(String strDate1, String strDate2) {
		int iMonth = 0;
		int flag = 0;
		try {
			Calendar objCalendarDate1 = Calendar.getInstance();
			objCalendarDate1.setTime(DateFormat.getDateInstance().parse(
					strDate1));

			Calendar objCalendarDate2 = Calendar.getInstance();
			objCalendarDate2.setTime(DateFormat.getDateInstance().parse(
					strDate2));

			if (objCalendarDate2.equals(objCalendarDate1))
				return 0;
			if (objCalendarDate1.after(objCalendarDate2)) {
				Calendar temp = objCalendarDate1;
				objCalendarDate1 = objCalendarDate2;
				objCalendarDate2 = temp;
			}
			if (objCalendarDate2.get(Calendar.DAY_OF_MONTH) < objCalendarDate1
					.get(Calendar.DAY_OF_MONTH))
				flag = 1;
			if (objCalendarDate2.get(Calendar.YEAR) > objCalendarDate1
					.get(Calendar.YEAR)) {
				iMonth = ((objCalendarDate2.get(Calendar.YEAR) - objCalendarDate1
						.get(Calendar.YEAR))
						* 12 + objCalendarDate2.get(Calendar.MONTH) - flag)
						- objCalendarDate1.get(Calendar.MONTH);
			} else {
				iMonth = objCalendarDate2.get(Calendar.MONTH)
						- objCalendarDate1.get(Calendar.MONTH) - flag;
			}
		} catch (Exception e) {
			System.err.print(e);
		}
		return iMonth;
	}

	/**
	 * 获取当前星期
	 * 
	 * @return 返回类型 String 当前星期几(一、二、三、四等)
	 */
	public static String getDayOfWeek() {
		String s = "";
		Calendar aCalendar = Calendar.getInstance();
		int x = aCalendar.get(Calendar.DAY_OF_WEEK);
		String a = Integer.toString(x - 1);
		if ("1".equals(a)) {
			s = "一";
		} else if ("2".equals(a)) {
			s = "二";
		} else if ("3".equals(a)) {
			s = "三";
		} else if ("4".equals(a)) {
			s = "四";
		} else if ("5".equals(a)) {
			s = "五";
		} else if ("6".equals(a)) {
			s = "六";
		} else if ("7".equals(a)) {
			s = "日";
		}
		return s;
	}

	public static void main(String[] args) {
		try {
			System.out.println(new DateUtil().parseToDate("2012-10-11 22:22"));
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/**
	 * 取得系统当前时间前n个月的相对应的一天
	 * 
	 * @param n
	 *            int
	 * @return String yyyy-mm-dd
	 */
	public static String getNMonthBeforeCurrentDay(int n) {
		Calendar c = Calendar.getInstance();
		c.add(c.MONTH, -n);
		return " " + c.get(c.YEAR) + "- " + (c.get(c.MONTH) + 1) + "- "
				+ c.get(c.DATE);

	}

	/**
	 * 取得系统当前时间后n个月的相对应的一天
	 * 
	 * @param n
	 *            int
	 * @return String yyyy-mm-dd
	 */
	public static String getNMonthAfterCurrentDay(int n) {
		Calendar c = Calendar.getInstance();
		c.add(c.MONTH, n);
		return " " + c.get(c.YEAR) + "- " + (c.get(c.MONTH) + 1) + "- "
				+ c.get(c.DATE);

	}

	/**
	 * 取得系统当前时间前n天
	 * 
	 * @param n
	 *            int
	 * @return String yyyy-mm-dd
	 */
	public static String getNDayBeforeCurrentDate(String dateStr,String dateFormat,int n) {
		try {
			Calendar c = Calendar.getInstance();
			Date date = parse(dateStr,dateFormat);
			c.setTime(date);
			c.add(c.DAY_OF_MONTH, -n);
			return c.get(c.YEAR) + "-" + (c.get(c.MONTH) + 1) + "-"
					+ c.get(c.DATE);
		} catch (ParseException e) {
			e.printStackTrace();
			return "";
		}
	}

	/**
	 * 取得系统当前时间后n天
	 * 
	 * @param n
	 *            int
	 * @return String yyyy-mm-dd
	 */
	public static String getNDayAfterCurrentDate(String dateStr,String dateFormat,int n) {
		try {
			Calendar c = Calendar.getInstance();
			Date date = parse(dateStr,dateFormat);
			c.setTime(date);
			c.add(c.DAY_OF_MONTH, n);
			return c.get(c.YEAR) + "-" + (c.get(c.MONTH) + 1) + "-"
					+ c.get(c.DATE);
		} catch (ParseException e) {
			e.printStackTrace();
			return "";
		}
	}

	/**
	 * 获取当前日期号样式：dd
	 * 
	 * @return 返回类型 String 当前日期号
	 * 
	 */
	public static String getDay() {
		return format(getCurrDate(), DD);
	}

	/**
	 * 按给定日期样式判断给定字符串是否为合法日期数据
	 * 
	 * @param strDate
	 *            要判断的日期
	 * @param pattern
	 *            日期样式
	 * @return 返回类型 boolean 如果是 返回true,否则返回false
	 * 
	 */
	public static boolean isDate(String strDate, String pattern) {
		try {
			parse(strDate, pattern);
			return true;
		} catch (ParseException pe) {
			return false;
		}
	}

	/**
	 * 判断给定字符串是否为特定格式年份（格式：yyyy）数据
	 * 
	 * @param strDate
	 *            要判断的日期
	 * @return 返回类型 boolean 如果是返回true，否则返回false
	 * 
	 */
	public static boolean isYYYY(String strDate) {
		try {
			parse(strDate, YYYY);
			return true;
		} catch (ParseException pe) {
			return false;
		}
	}

	/**
	 * 判断给定字符串是否为特定格式年月（格式：yyyy-MM）数据
	 * 
	 * @param strDate
	 *            要判断的日期字串
	 * @return 返回类型 boolean 如果是返回true，否则返回false
	 * 
	 */
	public static boolean isYYYY_MM(String strDate) {
		try {
			parse(strDate, YYYY_MM);
			return true;
		} catch (ParseException pe) {
			return false;
		}
	}

	/**
	 * 判断给定字符串是否为特定格式的年月日（格式：yyyy-MM-dd）数据
	 * 
	 * @param strDate
	 *            要判断的日期字串
	 * @return 返回类型 boolean 如果是返回true，否则返回false
	 * 
	 */
	public static boolean isYYYY_MM_DD(String strDate) {
		try {
			parse(strDate, YYYY_MM_DD);
			return true;
		} catch (ParseException pe) {
			return false;
		}
	}

	/**
	 * 判断给定字符串是否为特定格式年月日时分秒（格式：yyyy-MM-dd HH:mm:ss）数据
	 * 
	 * @param strDate
	 *            要判断的日期
	 * @return 返回类型 boolean 如果是返回true，否则返回false
	 * 
	 */
	public static boolean isYYYY_MM_DD_HH_MM_SS(String strDate) {
		try {
			parse(strDate, YYYY_MM_DD_HH_MM_SS);
			return true;
		} catch (ParseException pe) {
			return false;
		}
	}

	/**
	 * 判断给定字符串是否为特定格式时分秒（格式：HH:mm:ss）数据
	 * 
	 * @param strDate
	 *            要判断的日期
	 * @return 返回类型 boolean 如果是返回true，否则返回false
	 * 
	 */
	public static boolean isHH_MM_SS(String strDate) {
		try {
			parse(strDate, HH_MM_SS);
			return true;
		} catch (ParseException pe) {
			return false;
		}
	}

	/**
	 * 获取给定日期字串的后intevalDay天的日期
	 * 
	 * @param refenceDate
	 *            给定日期（格式为：yyyy-MM-dd）
	 * @param intevalDays
	 *            间隔天数
	 * @return 返回类型 String 计算后的日期
	 * 
	 */
	public static String getNextDate(String refenceDate, int intevalDays) {
		try {
			return getNextDate(parse(refenceDate, YYYY_MM_DD), intevalDays);
		} catch (Exception ee) {
			return "";
		}
	}

	/**
	 * 获取给定日期的后intevalDay天的日期
	 * 
	 * @param refenceDate
	 *            给定日期
	 * @param intevalDays
	 *            间隔天数
	 * @return 返回类型 String 计算后的日期
	 * 
	 */
	public static String getNextDate(Date refenceDate, int intevalDays) {
		try {
			Calendar calendar = Calendar.getInstance();
			calendar.setTime(refenceDate);
			calendar.set(Calendar.DATE, calendar.get(Calendar.DATE)
					+ intevalDays);
			return format(calendar.getTime(), YYYY_MM_DD);
		} catch (Exception ee) {
			return "";
		}
	}

	public static Date getNextDay(Date refenceDate, int intevalDays) {
		Date nd = null;
		try {
			nd = DateUtil.parse(DateUtil.getNextDate(refenceDate, intevalDays), DateUtil.YYYY_MM_DD);
		} catch (ParseException e) {
			e.printStackTrace();
		}

		return nd;
	}

	/**
	 * 获取给定日期字串的间隔天数
	 *
	 * @param startDate
	 *            起始时间字串 如 "2009-10-10"
	 * @param endDate
	 *            结束时间字串 如 "2009-12-10"
	 * @return 返回类型 long 间隔天数
	 *
	 */
	public static long getIntevalDays(String startDate, String endDate) {
		try {
			return getIntevalDays(parse(startDate, YYYY_MM_DD), parse(endDate,
					YYYY_MM_DD));
		} catch (Exception ee) {
			return 0l;
		}
	}

	/**
	 * 获取给定日期的间隔天数
	 *
	 * @param startDate
	 *            起始时间字串,如:new Date()
	 * @param endDate
	 *            结束时间字串,如:new Date()
	 * @return 返回类型 long 间隔天数
	 *
	 */
	public static long getIntevalDays(Date startDate, Date endDate) {
		try {
			Calendar startCalendar = Calendar.getInstance();
			Calendar endCalendar = Calendar.getInstance();

			startCalendar.setTime(startDate);
			endCalendar.setTime(endDate);
			long diff = endCalendar.getTimeInMillis()
					- startCalendar.getTimeInMillis();

			return (diff / (1000 * 60 * 60 * 24));
		} catch (Exception ee) {
			return 0l;
		}
	}

	/**
	 * 求当前日期和指定字符串日期的相差天数
	 *
	 * @param date
	 *            指定字符串日期 如: "2009-10-12"
	 * @return 返回类型 long 当前日期和指定字符串日期的相差天数
	 *
	 */
	public static long getTodayIntevalDays(String date) {
		try {
			Date currentDate = new Date();
			SimpleDateFormat myFormatter = new SimpleDateFormat("yyyy-MM-dd");
			Date theDate = myFormatter.parse(date);
			long days = (currentDate.getTime() - theDate.getTime())
					/ (24 * 60 * 60 * 1000);

			return days;
		} catch (Exception ee) {
			return 0l;
		}
	}

	/**
	 * 求当前日期和指定日期的相差天数
	 *
	 * @param date
	 *            指定字符串日期,如: new Date()
	 * @return 返回类型 long 当前日期和指定字符串日期的相差天数
	 *
	 */
	public static long getTodayIntevalDays(Date date) {
		try {

			Date currentDate = new Date();
			long days = (currentDate.getTime() - date.getTime())
					/ (24 * 60 * 60 * 1000);

			return days;
		} catch (Exception ee) {
			return 0l;
		}
	}

	/**
	 * 把指定时间字符串转换成日期型
	 *
	 * @param dateTimeStr
	 *            指定时间字符
	 * @return 返回类型 Date 转换后的日期
	 *
	 */
	public static Date parseToDate(String dateTimeStr) {
		if (dateTimeStr == null)
			return null;
		Date d = null;
		int formatStrLength = formatStr.length;
		for (int i = 0; i < formatStrLength; i++) {
			d = parseToDate2(dateTimeStr, formatStr[i]);
			if (d != null) {
				break;
			}
		}
		return d;
	}

	/**
	 * 把指定时间字符串转换成指定日期型
	 *
	 * @param dateTimeStr
	 *            指定时间字符
	 * @param formatString
	 *            指定时间日期类型,如：yyyy_MM_dd
	 * @return 返回类型 Date 转换后的日期
	 *
	 */
	private static Date parseToDate2(String dateTimeStr, String formatString) {
		Date d = null;
		SimpleDateFormat sdf = new SimpleDateFormat(formatString);
		try {
			d = sdf.parse(dateTimeStr);
		} catch (ParseException pe) {

		}
		return d;
	}

	/**
	 * 把指定日期时间转换为字符串
	 *
	 * @param datetime
	 *            指定日期时间
	 * @return 返回类型 String 日期时间字符串
	 */
	public static String dateTimeToString(Date datetime) {
		GregorianCalendar calendar = new GregorianCalendar();
		calendar.setTime(datetime);
		String dateTime = calendar.get(Calendar.YEAR) + ""
				+ (calendar.get(Calendar.MONTH) + 1 > 9 ? "" : "0")
				+ (calendar.get(Calendar.MONTH) + 1) + ""
				+ (calendar.get(Calendar.DATE) > 9 ? "" : "0")
				+ calendar.get(Calendar.DATE) + ""
				+ (calendar.get(Calendar.HOUR_OF_DAY) > 9 ? "" : "0")
				+ calendar.get(Calendar.HOUR_OF_DAY) + ""
				+ (calendar.get(Calendar.MINUTE) > 9 ? "" : "0")
				+ calendar.get(Calendar.MINUTE) + ""
				+ (calendar.get(Calendar.SECOND) > 9 ? "" : "0")
				+ calendar.get(Calendar.SECOND);
		return dateTime;
	}

	/**
	 * 根据指定年,月,日得到一周的第几天
	 *
	 * @param year
	 *            指定年
	 * @param month
	 *            指定月
	 * @param day
	 *            指定日
	 * @return 返回类型 int 得到一周的第几天
	 *
	 */
	public static int getDayOfWeek(int year, int month, int day) {
		Calendar cal = Calendar.getInstance();
		cal.clear();
		cal.set(year, month - 1, day);
		int dayofWeek = cal.get(Calendar.DAY_OF_WEEK);
		switch (dayofWeek) {
		case 1:
			dayofWeek = 7;
			break;
		case 2:
			dayofWeek = 1;
			break;
		case 3:
			dayofWeek = 2;
			break;
		case 4:
			dayofWeek = 3;
			break;
		case 5:
			dayofWeek = 4;
			break;
		case 6:
			dayofWeek = 5;
			break;
		case 7:
			dayofWeek = 6;
			break;
		}

		return dayofWeek;
	}

	/**
	 * 根据指定年,月,日得到一年的第几周
	 *
	 * @param year
	 *            指定年
	 * @param month
	 *            指定月
	 * @param day
	 *            指定日
	 * @return 返回类型 int 得到一年的第几周
	 *
	 */
	public static int getWeekOfYear(int year, int month, int day) {
		Calendar cal = Calendar.getInstance();
		cal.clear();
		cal.set(year, month - 1, day);
		// SimpleDateFormat formatter = new SimpleDateFormat("dd/MMM/yyyy");
		// System.err.println(formatter.format(cal.getTime()));
		return cal.get(Calendar.WEEK_OF_YEAR);
	}

	/**
	 * 根据指定年,月,日得到一月的第几天
	 *
	 * @param year
	 *            指定年
	 * @param month
	 *            指定月
	 * @param day
	 *            指定日
	 * @return 返回类型 int 得到一月的第几天
	 *
	 */
	public static int getDayOfMonth(int year, int month, int day) {
		Calendar cal = Calendar.getInstance();
		cal.clear();
		cal.set(year, month - 1, day);
		return cal.get(Calendar.DAY_OF_MONTH);
	}

	/**
	 * 根据指定年,得到上一月
	 *
	 * @param month
	 *            指定月
	 * @return 返回类型 String 返回上月是第几月
	 *
	 */
	public static String getFrontMonth(String month) {
		int mon = Integer.parseInt(month);
		String frontmonth = "";
		if (mon == 1) {
			frontmonth = "12";
		} else if (mon < 11) {
			frontmonth = "0" + String.valueOf(mon - 1);
		} else {
			frontmonth = String.valueOf(mon - 1);
		}
		return frontmonth;
	}

	/**
	 * 根据指定年,月得到下一月
	 *
	 * @param month
	 *            指定月
	 * @return 返回类型 String 返回下月是第几月
	 *
	 */
	public static String getNextMonth(String month) {
		int year = Integer.parseInt(month.substring(0, 4));
		int mon = Integer.parseInt(month.substring(4, 6));
		String nextmonth = "";
		String nextyear = String.valueOf(year);
		if (mon == 12) {
			nextmonth = "01";
			nextyear = String.valueOf(year + 1);
		} else if (mon < 9) {
			nextmonth = "0" + String.valueOf(mon + 1);
		} else {
			nextmonth = String.valueOf(mon + 1);
		}
		return nextyear + nextmonth;
	}

	/**
	 * 根据指定年月字符串得到下个季度的月份
	 *
	 * @param yearAndMonth
	 *            指定年月字符 如"200910"
	 * @return 返回类型 String 下个季度的月份 如"201001"
	 *
	 */
	public static String getMonthOfNextQuarter(String yearAndMonth) {
		int year = Integer.parseInt(yearAndMonth.substring(0, 4));
		int mon = Integer.parseInt(yearAndMonth.substring(4, 6));
		String nextmonth = "";
		String nextyear = String.valueOf(year);
		if (mon == 10) {
			nextmonth = "01";
			nextyear = String.valueOf(year + 1);
		} else if (mon == 11) {
			nextmonth = "02";
			nextyear = String.valueOf(year + 1);
		} else if (mon == 12) {
			nextmonth = "03";
			nextyear = String.valueOf(year + 1);
		} else if (mon < 7) {
			nextmonth = "0" + String.valueOf(mon + 3);
		} else {
			nextmonth = String.valueOf(mon + 3);
		}
		return nextyear + nextmonth;
	}

	/**
	 * 求当前日期和指定字符串日期的相差小时数
	 *
	 * @param date
	 *            指定字符串日期
	 * @return 返回类型 long 当前日期和指定字符串日期的相差小时数
	 *
	 */

	public static long getIntevalHours(String date) {
		try {
			Date currentDate = new Date();

			SimpleDateFormat myFormatter = new SimpleDateFormat(
					YYYY_MM_DD_HH_MM_SS);
			Date theDate = myFormatter.parse(date);

			long hours = (currentDate.getTime() - theDate.getTime())
					/ (60 * 60 * 1000);

			return hours;
		} catch (Exception ee) {
			return 0l;
		}
	}

	/**
	 * 求两日期对象间的小时数间隔
	 *
	 * @param startDate
	 *            起始日期
	 * @param endDate
	 *            结束日期
	 * @return 返回类型 long 两日期对象间的小时数间隔
	 *
	 */
	public static long getIntevalHours(Date startDate, Date endDate) {
		try {
			long hours = 0;
			if (endDate == null) {
				hours = ((new Date()).getTime() - startDate.getTime())
						/ (60 * 60 * 1000);
			} else {
				hours = (endDate.getTime() - startDate.getTime())
						/ (60 * 60 * 1000);
			}

			return hours;
		} catch (Exception ee) {
			return 0;
		}
	}

	/**
	 * 将指定日期格式化成"yyyy年M月d日"的形式，如将2000-01-01转换为2000年1月1日
	 *
	 * @param date
	 *            指定日期
	 * @return 返回类型 String 格式化后日期字串
	 *
	 */
	public static String getChineseDate(Date date) {
		DateFormat df = DateFormat.getDateInstance(DateFormat.LONG,
				Locale.CHINESE);
		String strDate = df.format(date);
		return strDate;
	}

	/**
	 * 将日期格式化成"yyyy-MM-dd HH:mm:ss"的形式，如"2000-12-3 12:53:48"
	 *
	 * @param date
	 *            指定日期
	 * @return 返回类型 String 格式化后日期字串
	 *
	 */
	public static String getLongFormatTime(Date date) {
		DateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		String strDate = df.format(date);
		return strDate;
	}

	/**
	 * 将指定日期格式化成"yyyy-MM-dd"的形式，如"2000-12-3"
	 * 
	 * @param date
	 *            指定日期
	 * @return 返回类型 String 格式化后的时间字串
	 * 
	 */
	public static String getyyyyMMddDateStr(Date date) {
		DateFormat df = new SimpleDateFormat("yyyy-MM-dd");
		String strDate = df.format(date);
		return strDate;
	}

	/**
	 * 获得今天
	 * 
	 * @param begin
	 *            开始，如""
	 * @param end
	 *            结束，如:""
	 * @param now
	 *            现在，如:""
	 * @param calendar
	 *            GregorianCalendar对象
	 * 
	 */
	public static void calcToday(String begin, String end, String now,
			GregorianCalendar calendar) {

		DateUtil.begin = now;
		DateUtil.end = now;

	}

	/**
	 * 获得昨天
	 * 
	 * @param begin
	 *            开始 如""
	 * @param end
	 *            结束 如:""
	 * @param now
	 *            现在 如:""
	 * @param calendar
	 *            GregorianCalendar对象
	 * 
	 */
	public static void calcYesterday(String begin, String end, String now,
			GregorianCalendar calendar) {

		calendar.add(Calendar.DATE, -1);
		begin = new java.sql.Date(calendar.getTime().getTime()).toString();
		end = begin;
		DateUtil.begin = begin;
		DateUtil.end = end;

	}

	/**
	 * 获得本周
	 * 
	 * @param begin
	 *            开始 如""
	 * @param end
	 *            结束 如:""
	 * @param now
	 *            现在 如:""
	 * @param calendar
	 *            GregorianCalendar对象
	 * 
	 */
	public static void calcThisWeek(String begin, String end, String now,
			GregorianCalendar calendar) {
		end = now;
		int minus = calendar.get(Calendar.DAY_OF_WEEK) - 2;
		if (minus < 0) {

		} else {

			calendar.add(Calendar.DATE, -minus);
			begin = new java.sql.Date(calendar.getTime().getTime()).toString();
			DateUtil.begin = begin;
			DateUtil.end = end;

		}
	}

	/**
	 * 获得上一周
	 * 
	 * @param begin
	 *            开始 如""
	 * @param end
	 *            结束 如:""
	 * @param now
	 *            现在 如:""
	 * @param calendar
	 *            GregorianCalendar对象
	 * 
	 */
	public static void calcLastWeek(String begin, String end, String now,
			GregorianCalendar calendar) {

		calendar.setFirstDayOfWeek(Calendar.MONDAY);
		int minus = calendar.get(Calendar.DAY_OF_WEEK);

		calendar.add(Calendar.DATE, -minus + 1);
		end = new java.sql.Date(calendar.getTime().getTime()).toString();
		calendar.add(Calendar.DATE, -6);
		begin = new java.sql.Date(calendar.getTime().getTime()).toString();
		DateUtil.begin = begin;
		DateUtil.end = end;

	}

	/**
	 * 获得本月
	 * 
	 * @param begin
	 *            开始 如""
	 * @param end
	 *            结束 如:""
	 * @param now
	 *            现在 如:""
	 * @param calendar
	 *            GregorianCalendar对象
	 * 
	 */
	public static void calcThisMonth(String begin, String end, String now,
			GregorianCalendar calendar) {
		end = now;
		int dayOfMonth = calendar.get(Calendar.DATE);
		calendar.add(Calendar.DATE, -dayOfMonth + 1);
		begin = new java.sql.Date(calendar.getTime().getTime()).toString();
		DateUtil.begin = begin;
		DateUtil.end = end;

	}

	/**
	 * 获得上月
	 * 
	 * @param begin
	 *            开始 如""
	 * @param end
	 *            结束 如:""
	 * @param now
	 *            现在 如:""
	 * @param calendar
	 *            GregorianCalendar对象
	 */
	public static void calcLastMonth(String begin, String end, String now,
			GregorianCalendar calendar) {

		calendar.set(calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH),
				1);
		calendar.add(Calendar.DATE, -1);
		end = new java.sql.Date(calendar.getTime().getTime()).toString();

		int month = calendar.get(Calendar.MONTH) + 1;
		begin = calendar.get(Calendar.YEAR) + "-" + month + "-01";
		DateUtil.begin = begin;
		DateUtil.end = end;

	}

	/**
	 * 当返回值为true时表示指定时间与当前时间之差在24小时内，若为false则表示不在24小时之内
	 * 
	 * @param time
	 *            指定时间串 如:"12000293293"
	 * @return 返回类型 boolean 指定时间与当前时间之差在24小时内，若为false则表示不在24小时之内
	 */
	public static boolean dateCompare(String time) {
		Date date = new Date();

		Long dateLongValue = date.getTime();
		Long timeLongVlaue = 0L;
		if (time != null && !time.equals("")) {

			timeLongVlaue = Long.parseLong(time);
		}

		if ((dateLongValue - timeLongVlaue) <= DAY_MILLISECOND) {
			return true;
		}
		return false;
	}

	/**
	 * 当返回值为true时表示指定时间与当前时间之差在同一月，若为false则表示不在同一月
	 * 
	 * @param time
	 *            指定时间串 如:"12000293293"
	 * @return 返回类型 boolean 指定时间与当前时间之差在同一月，若为false则表示不在同一月
	 */
	public static boolean compare(String time) {
		Date currentDate = new Date();
		int currentDay;
		int compareDay;

		Long timeLongVlaue = 0L;
		if (time != null && !time.equals("")) {
			timeLongVlaue = Long.parseLong(time);
		}
		Date compareDate = new Date(timeLongVlaue);
		Calendar compareCalendar = Calendar.getInstance();
		Calendar currentCalendar = Calendar.getInstance();
		currentCalendar.setTime(currentDate);
		compareCalendar.setTime(compareDate);
		currentDay = currentCalendar.get(Calendar.DAY_OF_MONTH);
		compareDay = compareCalendar.get(Calendar.DAY_OF_MONTH);
		int temp = currentDay - compareDay;
		if (temp == 1 || temp == 0) {
			return true;
		}
		return false;
	}

	/**
	 * 求当前日期和指定字符串日期的相差天数
	 * 
	 * @param startDate
	 * @return
	 */
	public static long getIntevalHours(Timestamp startDate, Timestamp endDate) {
		try {
			long hours = 0;
			if (endDate == null) {
				hours = ((new Date()).getTime() - startDate.getTime())
						/ (60 * 60 * 1000);
			} else {
				hours = (endDate.getTime() - startDate.getTime())
						/ (60 * 60 * 1000);
			}

			return hours;
		} catch (Exception ee) {
			System.out.println(ee);
			return 0;
		}
	}

	/**
	 * 求两个日期相差的分钟数
	 * 
	 * @param startDate
	 * @return
	 */
	public static long getIntevalMinutes(String startDate, String endDate) {
		try {
			SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

			Date d1 = sdf.parse(startDate);
			Date d2 = sdf.parse(endDate);
			long minute = (d2.getTime() - d1.getTime()) / 1000 / 60;
			return minute;
		} catch (Exception ee) {
			System.out.println(ee);
			return 0;
		}
	}

	/**
	 * 得到连个日期间的月份差
	 * 
	 * @param fromDate
	 *            起始日期
	 * @param toDate
	 *            结束日期
	 * @return
	 */
	public static int getMonthsBetweenTwoDate(Date fromDate, Date toDate) {
		int iMonth = 0;
		int flag = 0;
		try {
			Calendar objCalendarDate1 = Calendar.getInstance();
			objCalendarDate1.setTime(fromDate);

			Calendar objCalendarDate2 = Calendar.getInstance();
			objCalendarDate2.setTime(toDate);

			if (objCalendarDate2.equals(objCalendarDate1))
				return 0;
			if (objCalendarDate1.after(objCalendarDate2)) {
				Calendar temp = objCalendarDate1;
				objCalendarDate1 = objCalendarDate2;
				objCalendarDate2 = temp;
			}
			if (objCalendarDate2.get(Calendar.DAY_OF_MONTH) < objCalendarDate1
					.get(Calendar.DAY_OF_MONTH))
				flag = 1;

			if (objCalendarDate2.get(Calendar.YEAR) > objCalendarDate1
					.get(Calendar.YEAR))
				iMonth = ((objCalendarDate2.get(Calendar.YEAR) - objCalendarDate1
						.get(Calendar.YEAR))
						* 12 + objCalendarDate2.get(Calendar.MONTH) - flag)
						- objCalendarDate1.get(Calendar.MONTH);
			else
				iMonth = objCalendarDate2.get(Calendar.MONTH)
						- objCalendarDate1.get(Calendar.MONTH) - flag;

		} catch (Exception e) {
			e.printStackTrace();
		}
		return iMonth;
	}

	/**
	 * 
	 * @param String
	 *            sourceTime 待转化的时间
	 * 
	 * @param String
	 *            dataFormat 日期的组织形式
	 * 
	 * @return long 当前时间的长整型格式,如 1309574632778
	 * 
	 */

	public static long string2Long(String sourceTime, String dataFormat) {

		long longTime = 0L;

		DateFormat f = new SimpleDateFormat(dataFormat);

		Date d = null;

		try {

			d = f.parse(sourceTime);

		} catch (ParseException e) {

			e.printStackTrace();

		}

		longTime = d.getTime();

		return longTime;

	}

	/**
	 * 
	 * 长整型转换为日期类型
	 * 
	 * @param long
	 *            longTime 长整型时间
	 * 
	 * @param String
	 *            dataFormat 时间格式
	 * 
	 * @return String 长整型对应的格式的时间
	 * 
	 */

	public static String long2String(long longTime, String dataFormat) {
		String str = "";
		try {
			Date d = new Date(longTime);
			SimpleDateFormat s = new SimpleDateFormat(dataFormat);
			str = s.format(d);
		} catch (Exception e) {
			str = "";
		}
		return str;

	}
	
	/**
	 * 
	 * @Title:  获取上个月的第一天
	 * @param @return
	 * @return 返回类型 
	 * @throws
	 */
    public static String  getLastMonthFirstDay(){
         //获取上个月 第一天
    	SimpleDateFormat sdf=new SimpleDateFormat("yyyy-MM-dd",Locale.CHINA);
        Calendar c=Calendar.getInstance();
          c.add(Calendar.MONTH, -1);
          c.set(Calendar.DAY_OF_MONTH, 1);
         return  sdf.format(c.getTime());
          
        
    }
    /**
     * 获取当前日历的上一个月的最后一天
     * @param cal
     * @return
     */
    public static String getLastMonthLastDay(){
    	SimpleDateFormat sdf=new SimpleDateFormat("yyyy-MM-dd",Locale.CHINA);
        Calendar c=Calendar.getInstance();
        c.set(Calendar.DAY_OF_MONTH, 0);
        return  sdf.format(c.getTime());
    }

}