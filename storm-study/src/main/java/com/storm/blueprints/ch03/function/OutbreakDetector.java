package com.storm.blueprints.ch03.function;

import storm.trident.operation.BaseFunction;
import storm.trident.operation.TridentCollector;
import storm.trident.tuple.TridentTuple;
import backtype.storm.tuple.Values;

public class OutbreakDetector extends BaseFunction {
	private static final long serialVersionUID = 1L;
	public static final int THRESHOLD = 10;

	@Override
	public void execute(TridentTuple tuple, TridentCollector collector) {
		String key = (String) tuple.getValue(0);
		Long count = (Long) tuple.getValue(1);

		if (count > THRESHOLD) {
			collector.emit(new Values("outbreak detected for [" + key + "]"));
		}
	}
}