package com.java.inaction.ch12;

import java.time.DayOfWeek;
import java.time.Duration;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.Month;
import java.time.Period;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoField;
import java.time.temporal.ChronoUnit;
import java.time.temporal.TemporalAdjuster;
import java.time.temporal.TemporalAdjusters;

public class Ch12Main {
	public static void main(String[] args) {
		LocalDate date = LocalDate.of(2017, 11, 24);
		System.out.println(date.getYear());
		System.out.println(date.getMonth());
		System.out.println(date.getMonthValue());
		System.out.println(date.getDayOfMonth());
		System.out.println(date.getDayOfWeek());
		System.out.println(date.lengthOfMonth());
		System.out.println(date.lengthOfYear());
		System.out.println(date.isLeapYear());
		LocalDate today = LocalDate.now();
		System.out.println(today);

		int year = date.get(ChronoField.YEAR);
		System.out.println(year);
		int month = date.get(ChronoField.MONTH_OF_YEAR);
		System.out.println(month);
		int day = date.get(ChronoField.DAY_OF_MONTH);
		System.out.println(day);

		// time
		LocalTime time = LocalTime.of(13, 45, 20);
		System.out.println(time);
		System.out.println(time.getHour());
		System.out.println(time.getMinute());
		System.out.println(time.getSecond());

		LocalDate.parse("2014-03-18");
		LocalTime.parse("13:45:20");

		// localDateTime
		System.out.println(LocalDateTime.of(2014, Month.MARCH, 18, 13, 45, 20));
		System.out.println(LocalDateTime.of(date, time));
		System.out.println(date.atTime(13, 45, 20));
		System.out.println(date.atTime(time));
		System.out.println(time.atDate(date));

		// duration
		System.out
				.println(Duration.between(time, LocalDateTime.of(date, time)));
		System.out.println(Period.between(LocalDate.of(2014, 3, 8),
				LocalDate.of(2014, 3, 18)));
		
		System.out.println(Duration.ofMinutes(3));
		System.out.println(Duration.of(3, ChronoUnit.MINUTES));
		System.out.println(Period.ofDays(10));
		LocalDate date1 = LocalDate.of(2014, 3, 18);
		LocalDate date2 = date1.withYear(1011);
		System.out.println(date1.hashCode());
		System.out.println(date2.hashCode());
		
		LocalDate date3 = LocalDate.of(2014, 3, 18);
		System.out.println(date3.hashCode());
		date3 = date3.with(ChronoField.MONTH_OF_YEAR, 9);
		date3 = date3.plusYears(2).minusDays(10);
		date3.withYear(2011);
		System.out.println(date3);
		System.out.println(date3.hashCode());
		
		// temporaladjusters
		LocalDate date4 = LocalDate.of(2014, 3, 18);
		LocalDate date5 = date4.with(TemporalAdjusters
				.nextOrSame(DayOfWeek.SUNDAY));
		LocalDate date6 = date5.with(TemporalAdjusters.lastDayOfMonth());
		System.out.println(date4);
		System.out.println(date5);
		System.out.println(date6);
		LocalDate date7 = date4.with(new NextWokingDay());
		System.out.println(date7);
		LocalDate date8 = date4.with(t -> {
			DayOfWeek dow = DayOfWeek.of(t.get(ChronoField.DAY_OF_WEEK));
			int dayToAdd = 1;
			if (dow == DayOfWeek.FRIDAY)
				dayToAdd = 3;
			else if (dow == DayOfWeek.SATURDAY)
				dayToAdd = 3;

			return t.plus(dayToAdd, ChronoUnit.DAYS);
		});
		System.out.println(date8);
		
		TemporalAdjuster nextWorkingDay = TemporalAdjusters.ofDateAdjuster(t -> {
			DayOfWeek dow = DayOfWeek.of(t.get(ChronoField.DAY_OF_WEEK));
			int dayToAdd = 1;
			if (dow == DayOfWeek.FRIDAY)
				dayToAdd = 3;
			else if (dow == DayOfWeek.SATURDAY)
				dayToAdd = 3;

			return t.plus(dayToAdd, ChronoUnit.DAYS);
		});
		System.out.println(date4.with(nextWorkingDay));
		
		//formatter
		String s1 = date.format(DateTimeFormatter.BASIC_ISO_DATE);
		String s2 = date.format(DateTimeFormatter.ISO_LOCAL_DATE);
		System.out.println(s1);
		System.out.println(s2);
		
		System.out.println(LocalDate.parse("20140318", DateTimeFormatter.BASIC_ISO_DATE));
		
		DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");
		String formattedDate = LocalDate.of(2014, 3, 18).format(formatter);
		System.out.println(formattedDate);
		LocalDate.parse(formattedDate, formatter); 
	}
}
