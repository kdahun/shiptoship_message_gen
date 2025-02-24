package com.nsone.generator.ui.util;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Locale;

import com.nsone.generator.system.constant.SystemConstMessage;

public class TimeString {
	//
	public static String getNow() {
		//
		DateTimeFormatter format = DateTimeFormatter.ofPattern("YYYY-MM-dd HH:mm:ss.SSSSSS", Locale.KOREA);
		return LocalDateTime.now().format(format);
	}
	
	public static String getNowYYYYMMddHHmmss() {
		//
		DateTimeFormatter format = DateTimeFormatter.ofPattern("YYYYMMddHHmmss", Locale.KOREA);
		return LocalDateTime.now().format(format);
	}
	
	public static String getLogDisplay(LocalDateTime localDateTime) {
		//
		return localDateTime.format(SystemConstMessage.formatterForLogDisplay);
	}
	
	public static String getVSIFormat(LocalDateTime localDateTime) {
		//
		return localDateTime.format(SystemConstMessage.formatterForVSI);
	}
	
	public static String getESIFormat(LocalDateTime localDateTime) {
		//
		return localDateTime.format(SystemConstMessage.formatterForESI);
	}
}
