package com.storm.blueprints.ch03.function;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.storm.blueprints.ch03.DiagnosisEvent;

import storm.trident.operation.BaseFunction;
import storm.trident.operation.TridentCollector;
import storm.trident.tuple.TridentTuple;

public class CityAssignment extends BaseFunction {
	private static final long serialVersionUID = 1L;
	private static final Logger LOG = LoggerFactory
			.getLogger(CityAssignment.class);
	private static Map<String, double[]> CITIES = new HashMap<>();
	static {
		double[] phl = { 39.8, -75.2 };
		CITIES.put("PHL", phl);
		double[] nyc = { 40.7, -74.0 };
		CITIES.put("NYC", nyc);
	}

	@Override
	public void execute(TridentTuple tuple, TridentCollector collector) {
		DiagnosisEvent diagnosis = (DiagnosisEvent) tuple.getValue(0);
		double leastDistance = Double.MAX_VALUE;
		String closestCity = "NONE";
		// 가장 근접한 도시 찾기 로직 생략
		closestCity = "NYC";

		// 값보내기
		List<Object> values = new ArrayList<>();
		values.add(closestCity);
		LOG.debug("Closest city to lat={}, lng={}", diagnosis.getLat(),
				diagnosis.getLng());
		collector.emit(values);
	}
}