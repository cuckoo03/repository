package com.storm.examples.trident;

import java.util.Arrays;

import storm.trident.operation.BaseFunction;
import storm.trident.operation.TridentCollector;
import storm.trident.tuple.TridentTuple;

public class LogParser extends BaseFunction {

	private static final long serialVersionUID = 1L;

	@Override
	public void execute(TridentTuple tuple, TridentCollector collector) {
		String log = tuple.getStringByField("logString");
		ShopLog event = parseLog(log);
		collector.emit(Arrays.<Object>asList(event));
	}

	private ShopLog parseLog(String log) {
		String[] tokens = log.split(",");
		return new ShopLog(tokens[0], Long.parseLong(tokens[1]),
				Long.parseLong(tokens[2]));
	}
}