package com.storm.examples.madvirus.trident;

import storm.trident.operation.BaseFilter;
import storm.trident.operation.BaseFunction;
import storm.trident.operation.TridentCollector;
import storm.trident.tuple.TridentTuple;

public class PrintFunction extends BaseFilter {

	@Override
	public boolean isKeep(TridentTuple tuple) {
		System.out.println(tuple);
		return false;
	}

}
