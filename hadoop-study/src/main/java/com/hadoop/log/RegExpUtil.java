package com.hadoop.log;

public class RegExpUtil {
	public static String validateApacheLog() {
		String regex1 = "^([\\d.]+)"; // Client IP (처음에 숫자형식(123.) 이 여러번 나오는
		// 것에 match)
		String regex2 = " (\\S+)"; // - (공백 아님)
		String regex3 = " (\\S+)"; // - (공백 아님)
		String regex4 = " \\[([\\w:/]+\\s[+\\-]\\d{4})\\]";
		// time
		// String regex4 = " \\[([\\w:/]+)";//
		// timezone
		// String regex5 = "\\s([+\\-]\\d{4})\\]";// timezone
		String regex6 = " \"(.+?)\""; // request method and url :
		// 따옴표(") ~ 따옴표(")
		String regex7 = " (\\d{3})"; // HTTP code : 숫자연속 3번
		String regex8 = " (\\d+|(.+?))"; // Number of bytes, -일 때도 있음.

		return regex1 + regex2 + regex3 + regex4 + regex6 + regex7 + regex8;
	}
}
