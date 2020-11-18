package com.castis.util;

public class StringUtil {

	static public boolean isNull(String str) {
		if( str == null || str.isEmpty() )
			return true;
		return false;
	}
}
