package com.lucene;

import java.text.DecimalFormat;

public class NumberUtils {
	public static final DecimalFormat formatter = new DecimalFormat(
			"0000000000");

	public static String pad(int number) {
		return formatter.format(number);
	}
}
