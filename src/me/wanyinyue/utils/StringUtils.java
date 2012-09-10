package me.wanyinyue.utils;

import org.springframework.stereotype.Component;


@Component(value = "stringUtils")
public class StringUtils {

	/**
	 * 判断uri是否以js,css等结尾
	 * 
	 * @param uri
	 * @param excludes
	 * @return
	 */
	public static boolean uriEndsWithExcludes(String uri, String[] excludes) {
		for (String ex : excludes) {
			if (uri.endsWith(ex)) {
				return true;
			}
		}
		return false;
	}

	/**
	 * 将字符串中的单词首字母大写，并去掉“-”，用空格分开，如果有and，换成&
	 * 
	 * @param attribute
	 * @return
	 */
	public static String firstLetterToUpper(String attribute) {
		String temp = "";
		String arr[] = attribute.trim().split("-");
		for (int j = 0; j < arr.length; j++) {
			if ("and".equals(arr[j])) {
				temp += " &amp; ";
			} else {
				arr[j] = Character.toUpperCase(arr[j].charAt(0))
						+ arr[j].substring(1);
				temp += arr[j] + " ";
			}
		}
		return temp.substring(0, temp.lastIndexOf(" "));
	}

	/**
	 * 缩短文本
	 * 
	 * @param str
	 * @param maxLength
	 * @param breakChars
	 * @param withEllipsis
	 * @return
	 */
	public static String truncate(String str, int maxLength, String breakChars,
			boolean withEllipsis) {
		if (str != null && str.length() > maxLength) {
			str = str.substring(0, maxLength) + (withEllipsis ? "..." : "");
		}
		return str;
	}
}
