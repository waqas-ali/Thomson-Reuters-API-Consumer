package com.fx.helpers;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.TimeZone;

public class DateHelper {

	public static Calendar getZeroedTimeDate(Date d) {
		Calendar cal = dateToCalendarUTC(d);
		cal.set(Calendar.HOUR_OF_DAY, 0);
		cal.set(Calendar.MINUTE, 0);
		cal.set(Calendar.SECOND, 0);
		cal.set(Calendar.MILLISECOND, 0);
		return cal;
	}

	public static Calendar getMaximumTimeDate(Date d) {
		Calendar cal = dateToCalendarUTC(d);
		cal.set(Calendar.HOUR_OF_DAY, 23);
		cal.set(Calendar.MINUTE, 59);
		cal.set(Calendar.SECOND, 59);
		cal.set(Calendar.MILLISECOND, 999);
		return cal;
	}

	public static Calendar dateToCalendarUTC(Date d) {
		Calendar cal = Calendar.getInstance(TimeZone.getTimeZone("UTC"));
		cal.setTime(d);
		return cal;
	}
	public static Calendar dateToCalendar(Date d) {
		Calendar cal = Calendar.getInstance(TimeZone.getDefault());
		cal.setTime(d);
		return cal;
	}
	
	public static String getStandardFormatUTC(Date d) {
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		sdf.setTimeZone(TimeZone.getTimeZone("UTC"));
		return sdf.format(d);
	}
	public static String getStandardFormat(Date d) {
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		return sdf.format(d);
	}
	
	public static String calendarToStringUTC(Calendar cal) {
		try {
			Date date = cal.getTime();
			String strDate = getStandardFormatUTC(date);
			return strDate;
		}catch(Exception e) {
			return null;
		}
	}
}
