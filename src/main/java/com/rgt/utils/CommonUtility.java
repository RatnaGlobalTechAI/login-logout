package com.rgt.utils;

import java.util.Date;

import org.joda.time.DateTime;
import org.joda.time.format.DateTimeFormat;

public class CommonUtility {
	
	public static final String Date_Formate = "yyyy-MM-dd HH:mm:ss.S";
	
	public static Date convertDateStringToDate(String dateStr, String dateFormat) {
		Date dateObj = null;
		try {
			DateTime dateTime = DateTime.parse(dateStr, DateTimeFormat.forPattern(dateFormat));
			dateObj = dateTime.toLocalDate().toDate();
		} catch (Exception ex) {
			ex.printStackTrace();
		}
		return dateObj;
	}
	
	public static java.sql.Timestamp getCurrentTimeStamp() {
		Date today = new Date();
		return new java.sql.Timestamp(today.getTime());
	}
	
}
