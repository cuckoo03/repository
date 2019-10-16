package com.elasticsearch.util

import groovy.transform.TypeChecked
import java.text.SimpleDateFormat

@TypeChecked
class DateUtil {
	private static SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd");
	public static List<String> getDaysFromAToB( String startDate, String endDate ) {
		List<String> days = new ArrayList<String>();
		int gap = getDiffDayCount( startDate, endDate ) ;
		Calendar cal = Calendar.getInstance();

		try {
			cal.setTime(sdf.parse(endDate));
		} catch (Exception e) {
		}
		cal.add(Calendar.DATE, 1);

		for( int i = gap; i > -1; i-- ) {
			cal.add(Calendar.DATE, -1);
			days.add( sdf.format(cal.getTime()) );
		}


		return days;
	}
	/**
	 *
	 * @param fromDate yyyyMMdd
	 * @param toDate yyyyMMdd
	 * @return
	 */
	public static int getDiffDayCount(String fromDate, String toDate) {

		try {
			return (int) ((sdf.parse(toDate).getTime() - sdf.parse(fromDate).getTime()) / 1000 / 60 / 60 / 24);
		} catch (Exception e) {
			return 0;
		}
	}
}
