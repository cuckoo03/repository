package com.storm.cookbook.ch01.click.bolt;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

public class CountryStats {
	private int countryTotal = 0;
	private static final int COUNT_INDEX = 0;
	private static final int PERCENTAGE_INDEX = 1;
	private String country;
	private Map<String, List<Integer>> cityStats = new HashMap<>();

	public CountryStats(String country) {
		this.country = country;
	}

	public void cityFound(String cityName) {
		countryTotal++;
		if (cityStats.containsKey(cityName)) {
			cityStats.get(cityName).set(COUNT_INDEX,
					cityStats.get(cityName).get(COUNT_INDEX).intValue() + 1);
		} else {
			List<Integer> list = new LinkedList<>();
			list.add(1);
			list.add(0);
			cityStats.put(cityName, list);
		}

		double percent = cityStats.get(cityName).get(COUNT_INDEX)
				/ countryTotal;
		cityStats.get(cityName).set(PERCENTAGE_INDEX, (int) percent);
	}

	public int getCountryTotal() {
		return countryTotal;
	}

	public int getCityTotal(String cityName) {
		return cityStats.get(cityName).get(COUNT_INDEX).intValue();
	}
}