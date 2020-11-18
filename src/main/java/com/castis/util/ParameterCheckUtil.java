package com.castis.util;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public class ParameterCheckUtil {

	public static boolean checkDate(String date, String format) {
		if( StringUtil.isNull(date) )
			return false;
		
		try {
			DateUtil.getDate(date, format);
		} catch(Exception e) {
			log.error("Failed to convert date, date[{}], format[{}], error[{}]"
					, date, format, e.getMessage());
			return false;
		}
		
		return true;
	}
	
}
