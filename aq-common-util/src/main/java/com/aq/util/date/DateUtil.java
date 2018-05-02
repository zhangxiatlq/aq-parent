package com.aq.util.date;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.Locale;

import org.springframework.util.Assert;
/**
 * 
 * @ClassName: DateUtil
 * @Description: 时间传唤
 * @author: lijie
 * @date: 2018年1月26日 上午11:13:34
 */
public class DateUtil {
	
	public static final String YYYYMMDD = "yyyy-MM-dd";
	
	public static final String YYYYMMDDHHMMSS = "yyyy-MM-dd HH:mm:ss";
	/**
	 * 
	* @Title: getDayOfMonth  
	* @Description: 获取当前月的天数
	* @param @return    参数  
	* @return int    返回类型  
	* @throws
	 */
	public static int getDayOfMonth() {
		Calendar aCalendar = Calendar.getInstance(Locale.CHINA);
		return aCalendar.getActualMaximum(Calendar.DATE);
	}
	/**
	 * @throws ParseException 
	 * @throws ParseException 
	 * 
	* @Title: addMonth  
	* @Description: 时间加一个月 
	* @param @param date
	* @param @return    参数  
	* @return Date    返回类型  
	* @throws
	 */
	public static Date addMonth(Date date) throws ParseException {
		Calendar calendar = new GregorianCalendar();
		calendar.setTime(date);
		calendar.add(Calendar.MONTH, 1);
		calendar.add(Calendar.DAY_OF_MONTH, -1);
		String time = genFormatter(YYYYMMDD).format(calendar.getTime()) + " 23:59:59";
		return genFormatter(YYYYMMDDHHMMSS).parse(time);
	}
	public static Date addMonth(Date date,int month) throws ParseException {
		Calendar calendar = new GregorianCalendar();
		calendar.setTime(date);
		calendar.add(Calendar.MONTH, month);
		calendar.add(Calendar.DAY_OF_MONTH, -1);
		String time = genFormatter(YYYYMMDD).format(calendar.getTime()) + " 23:59:59";
		return genFormatter(YYYYMMDDHHMMSS).parse(time);
	}
	
	/**
	 * 
	 * @Title: getDate
	 * @author: lijie 
	 * @Description: 根据时间加天数
	 * @param time
	 * @param day
	 * @return
	 * @return: Date
	 */
	public static Date getAddDayDate(Date time, int day) {
		Assert.notNull(time, "add Day Date time is null");
		Calendar c = Calendar.getInstance();
		c.setTime(time);
		c.add(Calendar.DAY_OF_MONTH, day);
		return c.getTime();
	}
	/**
	 * 
	 * @Title: format
	 * @author: lijie 
	 * @Description: TODO
	 * @param date
	 * @param formatter
	 * @return
	 * @throws ParseException
	 * @return: Date
	 */
	public static Date format(Date date,SimpleDateFormat formatter) throws ParseException {
		Assert.notNull(formatter, "SimpleDateFormat is null");
		Assert.notNull(date, "Date is null");
		return formatter.parse(formatter.format(date));
	}
	/**
	 * 
	 * @Title: genFormatter
	 * @author: lijie 
	 * @Description: TODO
	 * @param pattern
	 * @return
	 * @return: SimpleDateFormat
	 */
	public static SimpleDateFormat genFormatter(String pattern){
		
		return new SimpleDateFormat(pattern);
	}
	/**
	 * 
	* @Title: isToday  
	* @Description: 判断是否是当天 
	* @param: @param date
	* @param: @return
	* @param: @throws Exception
	* @return boolean
	* @author lijie
	* @throws
	 */
	public static boolean isToday(Date date) {
		if (null != date) {
			Calendar to = Calendar.getInstance();
			to.setTime(date);
			int toyear = to.get(Calendar.YEAR);
			int tomonth = to.get(Calendar.MONTH) + 1;
			int today = to.get(Calendar.DAY_OF_MONTH);

			to.setTime(new Date());
			int mcyear = to.get(Calendar.YEAR);
			int mcmonth = to.get(Calendar.MONTH) + 1;
			int mcday = to.get(Calendar.DAY_OF_MONTH);
			if (toyear == mcyear && tomonth == mcmonth && today == mcday) {
				return true;
			}
		}
		return false;
	}
}
