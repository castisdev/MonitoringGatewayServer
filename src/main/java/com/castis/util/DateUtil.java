package com.castis.util;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;
import java.util.TimeZone;

public class DateUtil {
	
	/**
	 * 해당 날짜의 00:00:00 시간의 Date 타입 객체를 얻는다.
	 * 
	 * @return
	 */
	public static Date getTodayFirstTime() {
		Calendar todayLastCalendar= Calendar.getInstance();
		todayLastCalendar.set(Calendar.HOUR_OF_DAY, 0);
		todayLastCalendar.set(Calendar.MINUTE, 0);
		todayLastCalendar.set(Calendar.SECOND, 0);
		todayLastCalendar.set(Calendar.MILLISECOND, 0);
		
		return todayLastCalendar.getTime();		
	}
	
	/**
	 * 해당 날짜의 23:59:59 시간의 Date 타입 객체를 얻는다.
	 * 
	 * @return
	 */
	public static Date getTodayLastTime() {
		Calendar todayLastCalendar= Calendar.getInstance();
		todayLastCalendar.set(Calendar.HOUR_OF_DAY, 23);
		todayLastCalendar.set(Calendar.MINUTE, 59);
		todayLastCalendar.set(Calendar.SECOND, 59);
		todayLastCalendar.set(Calendar.MILLISECOND, 999);
		
		return todayLastCalendar.getTime();		
	}
	
	/**
	 * 현재시간에서 파라메터값의 분 만큼을 더한 Date 타입 객체를 얻는다.
	 * 
	 * @param minute
	 * @return
	 */
	public static Date getAddTime(int minute) {
		Calendar todayCalendar= Calendar.getInstance();
		todayCalendar.add(Calendar.MINUTE, minute);
		
		return todayCalendar.getTime();
	}
	
	/**
	 * 기준시간에서 파라메터값의 분 만큼을 더한 Date 타입 객체를 얻는다.
	 * 
	 * @param minute
	 * @return
	 */
	public static Date getAddSecond(Date baseTime, int second) {
		Calendar baseCalendar= Calendar.getInstance();
		baseCalendar.setTime(baseTime);
		baseCalendar.add(Calendar.SECOND, second);
		
		return baseCalendar.getTime();
	}
	@SuppressWarnings("deprecation")
	public static Date getAddSecondDivHalf(Date baseTime, int second) {
		int currentSecond = baseTime.getSeconds();
		if(currentSecond/30 < 1)
			baseTime.setSeconds(0);
		else
			baseTime.setSeconds(30);
		
		Calendar baseCalendar= Calendar.getInstance();
		baseCalendar.setTime(baseTime);
		baseCalendar.add(Calendar.SECOND, second);
		
		return baseCalendar.getTime();
	}
	public static Date getAddMinute(Date baseTime, int minute) {
		Calendar baseCalendar= Calendar.getInstance();
		baseCalendar.setTime(baseTime);
		baseCalendar.add(Calendar.MINUTE, minute);
		
		return baseCalendar.getTime();
	}
	public static Date getAddDay(Date baseTime, int day) {
		Calendar baseCalendar= Calendar.getInstance();
		baseCalendar.setTime(baseTime);
		baseCalendar.add(Calendar.DAY_OF_MONTH, day);
		
		return baseCalendar.getTime();
	}
	public static Date getAddDay(int day) {
		Calendar todayCalendar= Calendar.getInstance();
		todayCalendar.add(Calendar.DAY_OF_MONTH, day);
		
		return todayCalendar.getTime();
	}
	public static Date getAddHour(Date baseTime, int hour) {
		Calendar baseCalendar= Calendar.getInstance();
		baseCalendar.setTime(baseTime);
		baseCalendar.add(Calendar.HOUR, hour);
		
		return baseCalendar.getTime();
	}
	public static Date getAddMonth(Date baseTime, int month) {
		Calendar baseCalendar= Calendar.getInstance();
		baseCalendar.setTime(baseTime);
		baseCalendar.add(Calendar.MONTH, month);
		
		return baseCalendar.getTime();
	}
	
	
	/**
	 * 입력된 시간에 캐쉬가 갱신될 수 있도록 millisecond 단위로 1이 작은 Date 값을 반환
	 * 
	 * @param hour
	 * @param minute
	 * @param isTomorrow
	 * @return
	 */
	public static Date getExpiredTime(int hour, int minute, boolean isTomorrow) {
		Calendar todayCalendar= Calendar.getInstance();
		if(isTomorrow) {
			todayCalendar.add(Calendar.DAY_OF_MONTH, 1);
		}
		todayCalendar.set(Calendar.HOUR_OF_DAY, hour);
		todayCalendar.set(Calendar.MINUTE, minute);
		todayCalendar.set(Calendar.SECOND, 0);
		todayCalendar.set(Calendar.MILLISECOND, 0);
		todayCalendar.add(Calendar.MILLISECOND, -1);
		
		return todayCalendar.getTime();
	}
	
	/**
	 * format : "yyyy-MM-dd HH:mm:ss.SSS"
	 * */
	public static String getDateString(String format) {
		
		return getDateString(new Date(), format);
	}
	
	/**
	 * format : "yyyy-MM-dd HH:mm:ss.SSS"
	 * @throws ParseException 
	 * */
	public static String getDateString(String date_string, String format, String result_format) throws ParseException {
		
		SimpleDateFormat simpleDateFormat = new SimpleDateFormat(format);
		Date date = simpleDateFormat.parse(date_string);
		
		return getDateString(date, result_format);
	}
	
	/**
	 * format : "yyyy-MM-dd HH:mm:ss.SSS"
	 * */
	public static String getDateString(long date, String format) {
		return getDateString(new Date(date), format);
	}
	
	/**
	 * format : "yyyy-MM-dd HH:mm:ss.SSS"
	 * */
	public static String getDateString(Date date, String format) {
		
		SimpleDateFormat simpleDateFormat = new SimpleDateFormat(format);
		String dateString = simpleDateFormat.format(date);
		
		return dateString;
	}
	
	/**
	 * format : "input dateString"
	 * */
	public static Date getDate(String dateString, String format) throws Exception {
	    Date date = null;
	    try {
	    	SimpleDateFormat simpleDateFormat = new SimpleDateFormat(format);
		    date = simpleDateFormat.parse(dateString);
	    } catch (ParseException e) {
		    e.printStackTrace();
		    throw new Exception(e.getMessage());
	    }
	    return date;
	}

	public static int getDayOfWeek(Date date) throws Exception {
		Calendar cal = Calendar.getInstance();
		cal.setTime(date);
		int dayOfWeek = cal.get(Calendar.DAY_OF_WEEK);
		dayOfWeek = dayOfWeek==1 ? 7 : dayOfWeek-1;
		return dayOfWeek;
	}
	
	/**
	 * 입력된 날의 다음 달 날짜 정보를 반환한다.
	 * 
	 * @param date
	 *            Date 타입의 날짜 정보
	 * @return 입력날짜의 다음 월 날짜 Date 객체. 입력날짜가 연말이었다면 다음 해의 값이 올 수도 있다.<br>
	 *         일 이하의 값은 변화가 없다.
	 */
	public static Date getNextMonth(Date date) {
		Calendar cal = Calendar.getInstance();
		cal.setTime(date);
		cal.add(Calendar.MONTH, 1);
		Date resultDate = cal.getTime();
		return resultDate;
	}

	/**
	 * 입력된 날의 다음 날 날짜 정보를 반환한다.
	 * 
	 * @param date
	 *            Date 타입의 날짜 정보
	 * @return 입력날짜의 다음 날 날짜 Date 객체. 입력날짜가 월말이나 연말이었다면 다음 월 또는 다음 해의 값이 올 수도
	 *         있다.<br>
	 *         시간 이하의 값은 변화가 없다.
	 */
	public static Date getNextDate(Date date) {
		Calendar cal = Calendar.getInstance();
		cal.setTime(date);
		cal.add(Calendar.DAY_OF_MONTH, 1);
		Date resultDate = cal.getTime();
		return resultDate;
	}

	
	/**
	 * Date 형식의 데이터를 문자형 날짜값으로 변환한다.
	 * 
	 * @param date
	 *            Date 형식의 날짜 데이터
	 * @return yyyyMMdd 형식의 날짜 문자값
	 */
	public static String date2String(Date date) {
		return date2String(date, "yyyyMMdd");
	}

	/**
	 * Date 형식의 데이터를 문자형 날짜값으로 변환한다.
	 * 
	 * @param date
	 *            Date 형식의 날짜 데이터
	 * @param dateFormat
	 *            변환하고 싶은 날짜 문자형의 format. java의 Date format에 맞아야 함.
	 * @return dateFormat 형식에 맞춘 날짜 문자값
	 */
	public static String date2String(Date date, String dateFormat) {
		if (date == null) {
			return null;
		}
		java.text.SimpleDateFormat format = new SimpleDateFormat(dateFormat);
		String result = "";

		result = format.format(date);
		return result;
	}
	/**
	 * Date 정보를 입력받아 월을 반환한다.
	 * 
	 * @param date
	 * @return 월 (01 ~ 12)
	 */
	public static String getMonth(Date date) {
		return getMonth(date2String(date));
	}
	
	/**
	 * date yyyyMMdd 또는 yyyy-MM-dd 형식의 날짜값에서 월을 반환한다.
	 * 
	 * @param date date yyyyMMdd 또는 yyyy-MM-dd 형식의 날짜값
	 * @return 월 (01 ~ 12)
	 */
	public static String getMonth(String date) {
		String retDate = "";
		retDate = date.replaceAll("-", "");
		return retDate.substring(4, 6);
	}
	
	/**
	 * date yyyyMMdd 또는 yyyy-MM-dd 형식의 날짜값에서 연도를 반환한다.
	 * 
	 * @param date yyyyMMdd 또는 yyyy-MM-dd 형식의 날짜값
	 * @return 연도
	 */
	public static String getYear(String date) {
		String retDate = "";
		retDate = date.replaceAll("-", "");
		return retDate.substring(0, 4);
	}
	
	/**
	 * Date 정보를 입력받아 연도를 반환한다.
	 * 
	 * @param date
	 * @return 연도
	 */
	public static String getYear(Date date) {
		return getYear(date2String(date));
	}

	/**
	 * date yyyyMMdd 또는 yyyy-MM-dd 형식의 날짜값에서 일을 반환한다.
	 * 
	 * @param date date yyyyMMdd 또는 yyyy-MM-dd 형식의 날짜값
	 * @return 일 (01 ~ 31)
	 */
	public static String getDay(String date) {
		String retDate = "";
		retDate = date.replaceAll("-", "");
		return retDate.substring(6);
	}
	
	/**
	 * Date 정보를 입력받아 일을 반환한다.
	 * 
	 * @param date
	 * @return 일 (01 ~ 31)
	 */
	public static String getDay(Date date) {
		return getDay(date2String(date));
	}
	
	/**
	 * Date 정보를 입력받아 요일을 반환한다.
	 * 
	 * @param date
	 * @return 요일 (일,월,화,수,목,금,토)
	 */
	public static String getWeek(Date date) {
		SimpleDateFormat format = new SimpleDateFormat("EEE",Locale.KOREAN);
		return format.format(date);
	}
	/**
	 * 시작일과 종료일 두 날짜 사이의 일수를 반환한다.
	 * 
	 * @param startDate 시작일값
	 * @param endDate 종료일값
	 * @return
	 */
	public static int getPeriodDayLength(Date startDate, Date endDate) {
		long diff = (endDate.getTime() - startDate.getTime()) / (24 * 60 * 60 * 1000);
		return (int) diff + 1;
	}
	
	public static String utcToLocalTime(String datetime) throws Exception {
		String locTime = null;
		//TimeZone tz = TimeZone.getTimeZone("GMT+08:00"); 해당 국가 일시 확인 할 때, 한쿸은 +9
		TimeZone tz = TimeZone.getDefault();
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'");
		SimpleDateFormat sdf2 = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		
		try {
			Date parseDate = sdf.parse(datetime);
			long milliseconds = parseDate.getTime();
			int offset = tz.getOffset(milliseconds);
			locTime = sdf2.format(milliseconds + offset);
			locTime = locTime.replace("+0000", "");
		} catch(Exception e) {
			e.printStackTrace();
			throw new Exception(e.toString());
		}
		
		return locTime;
	}
	
}
