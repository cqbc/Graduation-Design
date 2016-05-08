package com.cqupt.spider.test;

import java.text.SimpleDateFormat;
import java.util.Date;

public class DateTest {
	public static void main(String[] args) {
		long time = Long.parseLong("1462199735798");
		Date date = new Date(time);
		SimpleDateFormat sd = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		System.out.println(sd.format(date));

	}
}