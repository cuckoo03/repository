package com.storm.examples.madvirus.trident;

import java.util.Arrays;

import storm.trident.operation.BaseFunction;
import storm.trident.operation.TridentCollector;
import storm.trident.tuple.TridentTuple;

public class LogParser extends BaseFunction {

	private static final long serialVersionUID = 1L;

	@Override
	public void execute(TridentTuple tuple, TridentCollector collector) {
		String log = tuple.getStringByField("logString");
		System.out.println(Arrays.<Object> asList(log));
		collector.emit(Arrays.<Object> asList(log));
	}

}