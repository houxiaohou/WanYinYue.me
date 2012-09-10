package me.wanyinyue.utils;

import java.text.SimpleDateFormat;
import java.util.Date;

public class DateUtils {

	private static SimpleDateFormat formatter1 = new SimpleDateFormat(
			"yyyy-MM-dd HH:mm:ss");

	// private static SimpleDateFormat formatter2 = new
	// SimpleDateFormat("yyyy-MM-dd");

	/**
	 * 当date为null时出现error
	 */
	public static String getCompleteDate(Date date) {
		if (date != null) {
			return formatter1.format(date);
		} else {
			return "";
		}

	}

	/**
	 * 得到某个时间date距离当前的时间 非常粗略的设计
	 * date不能为null
	 * @param date
	 * @return
	 */
	public static String getTimeAgo(Date date) {
		String timeAgo = "";
		if (date != null) {
			long ms = new Date().getTime() - date.getTime();
			if (ms < 60 * 1000) {
				timeAgo = ms / 1000 + "秒前";
			} else if (ms < 60 * 60 * 1000) {
				timeAgo = ms / (60 * 1000) + "分钟前";
			} else if (ms < (long) 2 * 24 * 60 * 60 * 1000) {
				timeAgo = ms / (60 * 60 * 1000) + "小时前";
			} else if (ms < (long) 30 * 24 * 60 * 60 * 1000) {
				timeAgo = ms / (24 * 60 * 60 * 1000) + "天前";
			} else if (ms < (long) 365 * 24 * 60 * 60 * 1000) {
				timeAgo = ms / (30 * 24 * 60 * 60 * 1000) + "月前";
			} else {
				timeAgo = ms / (365 * 24 * 60 * 60 * 1000) + "年前";
			}
		}
		return timeAgo;
	}
	
	/**
	 * 两个日期之间隔着0点，至少是过了一天的0点
	 * @param now
	 * @param past
	 * @return
	 */
	@SuppressWarnings("deprecation")
	public static boolean isOneDayPassed(Date now, Date past){
		if(past==null||now.getYear()>=past.getYear()&&now.getMonth()>=past.getMonth()&&now.getDay()>past.getDay()){
			return true;
		}else
			return false;
	}

}
