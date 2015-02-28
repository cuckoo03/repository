package com.storm.examples.trident;

import storm.trident.operation.ReducerAggregator;
import storm.trident.tuple.TridentTuple;

public class Sum implements ReducerAggregator<Long> {

	private static final long serialVersionUID = 1L;

	@Override
	public Long init() {
		return 0L;
	}

	@Override
	public Long reduce(Long curr, TridentTuple tuple) {
		return curr + tuple.getLong(0);
	}
}