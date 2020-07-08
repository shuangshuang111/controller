package com.dongnao;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

public class DateUtil {
	
	/**
	 * 计算月份的天数
	 * 
	 * @param date
	 * @return
	 */
	public static int getDaysOfMonth(Date date) {
		Calendar calendar = Calendar.getInstance();
		calendar.setTime(date);
		return calendar.getActualMaximum(Calendar.DAY_OF_MONTH);
	}

	public static void main(String[] args) throws ParseException {
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
		System.out.println(getDaysOfMonth(sdf.parse("2015-02-2")));
		
		System.out.println(getDaysOfMonth(new Date()));
	}

}
