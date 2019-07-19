package com.fx.study.util;

import java.util.Calendar;
import java.util.Date;

public class Util {

	@SuppressWarnings("deprecation")
	public static Date[] GetDateTime(String timeStr)
	{
		Date[] date = new Date[2];
		switch (timeStr) {
		case "年龄":
		case "不限":
			date[0] = new Date(0, 0, 0);
			date[1] = new Date();
			break;
		case "18岁以下":
			date[0] = GetDate(-22);
			date[1] = GetDate(-18);
			break;
		case "23-26岁":
			date[0] = GetDate(-26);
			date[1] = GetDate(-23);
			break;
		case "27-35岁":
			date[0] = GetDate(-35);
			date[1] = GetDate(-27);
			break;
		case "35岁以上":
			date[0] = GetDate(-35);
			date[1] = new Date(0, 0, 0);
			break;
		default:
			break;
		}
		return date;
	}
	
	private static Date GetDate(int years)
	{
		Calendar cal = Calendar.getInstance();  
		cal.add(Calendar.YEAR, years);
		return cal.getTime();
	}
}
