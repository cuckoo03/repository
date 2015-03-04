package com.storm.blueprints.ch03.function;

import java.util.ArrayList;
import java.util.List;

import storm.trident.operation.BaseFunction;
import storm.trident.operation.TridentCollector;
import storm.trident.tuple.TridentTuple;

public class OutbreakDetector extends BaseFunction {
	private static final long serialVersionUID = 1L;
	public static final int THRESHOLD = 2;

	@Override
	public void execute(TridentTuple tuple, TridentCollector collector) {
		String key = (String) tuple.getValue(0);
		Long count = (Long) tuple.getValue(1);

		if (count > THRESHOLD) {
			List<Object> values = new ArrayList<>();
			values.add("outbreak detected for [" + key + "]");
			collector.emit(values);
		}
	}
}