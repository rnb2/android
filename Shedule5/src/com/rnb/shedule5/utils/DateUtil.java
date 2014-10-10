/**
 * 
 */
package com.rnb.shedule5.utils;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;
import java.util.TimeZone;


public final class DateUtil {

	private static Calendar calendar = Calendar.getInstance();
	
	public static final Date MAX_SQL_DATE = getDate(9999, 12, 31, 23, 59, 59);
	public static final Date MIN_SQL_DATE = getDate(1900, 1, 1, 1, 1, 1);
	public static final long MILLISECONDS_IN_HOUR = 60L * 60 * 1000;
	public static final long MILLISECONDS_IN_MINUTE = (60 * 1000) % 60; 
	public static final long MILLISECONDS_IN_DAY = MILLISECONDS_IN_HOUR * 24;
	
	synchronized public static int getDayOfWeekInt(Date date) {
		calendar.setTime(date);
		int dayOfWeek = calendar.get(Calendar.DAY_OF_WEEK);
		
		return dayOfWeek-1;
	}
	
	/**
	 * Час:мин
	 * @param hours
	 * @param mins
	 * @return
	 */
	public static String getHourMin(Integer hours, Integer mins){
		StringBuilder sb = new StringBuilder();
		
		if(hours == null || mins == null)
			return sb.toString();
		
		String hour = length(hours) == 1 ? "0" + hours : hours.toString();
		String min = length(mins) == 1 ? mins + "0" : mins.toString();
		
		sb.append(hour);
		sb.append(":");
		sb.append(min);
		
		
		return sb.toString();
	}
	
	/**
	 * Кол-во цифр в числе
	 * @param value должно быть > 0 
	 * @return
	 */
	private static int length(Integer value){
		if(value <= 0)
			return 1;
		
		return (int) Math.log10(value) + 1;
	}
	
	/**
	 * "MMMM,yyyy"
	 */
	public static final SimpleDateFormat DEFAULT_MONTH_YEAR_FORMAT_FULL = new SimpleDateFormat("MMMM,yyyy", Locale.getDefault());
	
	/**
	 * 
	 * @param date
	 * @return Август,2013
	 */
	public static String getStringFormated_MonthYearFull(Date date){
		return DEFAULT_MONTH_YEAR_FORMAT_FULL.format(date);
	}
	
	/**
	 * 
	 * @param date
	 * @return to_date(date, 'dd.mm.yyyy')
	 */
	public static String getDateNativeSQLfromat(Date date){
		return "TO_DATE('"+DateUtil.getStringFormated_Date(date)+"','dd.mm.yyyy')";
	}
	
	/**
	 * 
	 * @param date
	 * @return to_date(date, 'dd.mm.yyyy hh24:mi')
	 */
	public static String getDateTimeNativeSQLfromat(Date date){
		return "TO_DATE('"+DateUtil.getStringFormated_DateTime(date)+"','dd.mm.yyyy hh24:mi')";
	}
	/**
	 * "HH:mm"
	 */
	public static final SimpleDateFormat DEFAULT_TIME_FORMAT = new SimpleDateFormat("HH:mm", Locale.getDefault());
	/**
	 * "dd.MM.yyyy HH:mm"
	 */
	public static final SimpleDateFormat DEFAULT_DATE_TIME_FORMAT = new SimpleDateFormat("dd.MM.yyyy HH:mm", Locale.getDefault());
	/**
	 * "dd.MM.yyyy"
	 */
	public static final SimpleDateFormat DEFAULT_DATE_FORMAT = new SimpleDateFormat("dd.MM.yyyy", Locale.getDefault());
	/**
	 * "MM.yyyy"
	 */
	public static final SimpleDateFormat DEFAULT_MONTH_YEAR_FORMAT = new SimpleDateFormat("MM.yyyy", Locale.getDefault());
	/**
	 * "dd"
	 */
	public static final SimpleDateFormat DEFAULT_DAY_FORMAT = new SimpleDateFormat("dd", Locale.getDefault());

	
	
	
	synchronized public static boolean isDayOf(Date date) {
		boolean result = false;
		calendar.setTime(date);
		int dayOfWeek = calendar.get(Calendar.DAY_OF_WEEK);

		if (dayOfWeek == 1 || dayOfWeek == 7) {
			result = true;
		}

		return result;
	}
			
	/**
	 * 
	 * @param date1
	 * @param date2
	 * @return разницу между датами в днях
	 */
	synchronized public static Integer getDateDiffInDays(Date date1, Date date2){
		   Calendar calOne = Calendar.getInstance(TimeZone.getTimeZone("GMT"));
		   calOne.setTime(date1);
	       Calendar calTwo = Calendar.getInstance(TimeZone.getTimeZone("GMT"));
	       calTwo.setTime(date2);
	       
	       Calendar distinct = Calendar.getInstance();
	       distinct.setTime(new Date(calTwo.getTime().getTime() - calOne.getTime().getTime()));

	       Integer result = Integer.valueOf(distinct.get(Calendar.DAY_OF_YEAR) - 1 );
       return result;
	}

	public static String getStringFormated_Time(Date date){
		return DEFAULT_TIME_FORMAT.format(date);
	}
	
	public static String getStringFormated_DateTime(Date date){
		return DEFAULT_DATE_TIME_FORMAT.format(date);
	}

	public static String getStringFormated_Date(Date date){
		return DEFAULT_DATE_FORMAT.format(date);
	}
	
	public static String getStringFormated_MonthYear(Date date){
		return DEFAULT_MONTH_YEAR_FORMAT.format(date);
	}

	synchronized public static int getMonth(Date date){
		calendar.setTime(date == null ? new Date() : date);
		return calendar.get(Calendar.MONTH);
	}

	synchronized public static int getYear(Date date){
		calendar.setTime(date == null ? new Date() : date);
		return calendar.get(Calendar.YEAR);
	}
	
	synchronized public static Date getDate(int year, int month, int dayOfMonth, int hourOfDay, int minute, int second){
		calendar.set(Calendar.YEAR, year);
		calendar.set(Calendar.MONTH, month - 1);
		calendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);
		calendar.set(Calendar.HOUR_OF_DAY, hourOfDay);
		calendar.set(Calendar.MINUTE, minute);
		calendar.set(Calendar.SECOND, second);
		calendar.set(Calendar.MILLISECOND, 0);
		return calendar.getTime();
	}

	synchronized public static Date addMinutes(Date date, int minutes){
		calendar.setTime(date);
		calendar.add(Calendar.MINUTE, minutes);
		return calendar.getTime();
	}
	
	synchronized public static Date addHours(Date date, int hours){
		calendar.setTime(date);
		calendar.add(Calendar.HOUR_OF_DAY, hours);
		return calendar.getTime();
	}
	synchronized public static Date addDays(Date date, int days){
		calendar.setTime(date);
		calendar.add(Calendar.DAY_OF_MONTH, days);
		return calendar.getTime();
	}
	
	synchronized public static Date addMohths(Date date, int months){
		calendar.setTime(date);
		calendar.add(Calendar.MONTH, months);
		return calendar.getTime();
	}
	
	synchronized public static Date getTruncDate(Date date){
		if (date == null) return null;
		calendar.setTime(date);
		calendar.set(Calendar.HOUR_OF_DAY, 0);
		calendar.set(Calendar.MINUTE, 0);
		calendar.set(Calendar.SECOND, 0);
		calendar.set(Calendar.MILLISECOND, 0);
		return calendar.getTime();
	}
	
	synchronized public static Date getEndOfDay(Date date){
		calendar.setTime(date);
		calendar.set(Calendar.HOUR_OF_DAY, 23);
		calendar.set(Calendar.MINUTE, 59);
		calendar.set(Calendar.SECOND, 59);
		calendar.set(Calendar.MILLISECOND, 999);
		return calendar.getTime();
	}
	
	synchronized public static Date getNextDay(Date date){
		calendar.setTime(date);
		calendar.add(Calendar.DAY_OF_MONTH, +1);
		calendar.set(Calendar.HOUR_OF_DAY, 0);
		calendar.set(Calendar.MINUTE, 0);
		calendar.set(Calendar.SECOND, 0);
		calendar.set(Calendar.MILLISECOND, 0);
		return calendar.getTime();
	}	
	
	synchronized public static Date getDateFrom1TimeForm2(Date date1, Date date2){
		Calendar calOne = Calendar.getInstance(TimeZone.getTimeZone("Europe/Athens"));
			calOne.setTime(date1);
	    Calendar calTwo = Calendar.getInstance(TimeZone.getTimeZone("Europe/Athens"));
	    	calTwo.setTime(date2);
	       
		calendar.set(Calendar.YEAR, calOne.get(Calendar.YEAR));
		calendar.set(Calendar.MONTH, calOne.get(Calendar.MONTH));
		calendar.set(Calendar.DAY_OF_MONTH, calOne.get(Calendar.DAY_OF_MONTH));

		calendar.set(Calendar.HOUR_OF_DAY, calTwo.get(Calendar.HOUR_OF_DAY));
		calendar.set(Calendar.MINUTE, calTwo.get(Calendar.MINUTE));
		calendar.set(Calendar.SECOND, calTwo.get(Calendar.SECOND));
		calendar.set(Calendar.MILLISECOND, 0);
		return calendar.getTime();
	}
	
	public static long getDateDiffInMillis(Date date1, Date date2){
		return date1.getTime() - date2.getTime();
	}
	
	/**
	 * 
	 * @param date1
	 * @param date2
	 * @param scale округление до scale после запятой 
	 * @param mode RoundingMode.UP, DOWN
	 * @return
	 */
	public static double getDateDiffInHoursRounding(Date date1, Date date2, int scale, RoundingMode mode){
		double d= (double)getDateDiffInMillis(date1, date2) / MILLISECONDS_IN_HOUR;
		return new BigDecimal(d).setScale(scale, mode).doubleValue();
	}
	
	public static long getDateDiffInHours(Date date1, Date date2){
		return Math.round((double)getDateDiffInMillis(date1, date2) / MILLISECONDS_IN_HOUR);
	}
	
	public static int getDateDiffInHoursAsInt(Date date1, Date date2){
		return (int)getDateDiffInHours(date1, date2);
	}
	
	public static int[] getDiffHourMinute(Date date1, Date date2){
		int[] times = new int[2];
		
		long diff = date1.getTime() - date2.getTime();
		
		times[0] = Long.valueOf(diff/(60L * 60 * 1000)).intValue(); //Hour
		times[1] = Long.valueOf(diff/(60 * 1000) % 60).intValue(); //Minute
		
		return times;
	}
	
	public static boolean equalsDates(Date date1, Date date2){
		if(date1 == null)
			return false;
		if(date2 == null)
			return false;
		
		long time1 = getTruncDate(date1).getTime();
		long time2 = getTruncDate(date2).getTime();
		
		if(time1 == time2)
			return true;
		
		return false;
	}
	
	public static Date getFirstDayInMonth(Date date){
		Calendar calendar = Calendar.getInstance();
		calendar.setTime(date);
		calendar.set(Calendar.DAY_OF_MONTH, calendar.getActualMinimum(Calendar.DAY_OF_MONTH));
		calendar.set(Calendar.HOUR_OF_DAY, 0);
		calendar.set(Calendar.MINUTE, 0);
		calendar.set(Calendar.SECOND, 0);
		calendar.set(Calendar.MILLISECOND, 0);		
		return calendar.getTime();
	}
	
	public static Date getLasttDayInMonth(Date date){
		Calendar calendar = Calendar.getInstance();
		calendar.setTime(date);
		calendar.set(Calendar.DAY_OF_MONTH,
                calendar.getActualMaximum(Calendar.DAY_OF_MONTH));
        calendar.set(Calendar.HOUR_OF_DAY, 23);
        calendar.set(Calendar.MINUTE, 59);
        calendar.set(Calendar.SECOND, 59);
        calendar.set(Calendar.MILLISECOND, 999);
        return calendar.getTime();
	}
	
	public static int getDay(Date date){
		Calendar calendar = Calendar.getInstance();
		calendar.setTime(date);
		return calendar.get(Calendar.DAY_OF_MONTH);
	}
	
}
