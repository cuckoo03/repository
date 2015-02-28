package com.storm.examples.trident;

import java.util.PriorityQueue;

import storm.trident.operation.BaseAggregator;
import storm.trident.operation.TridentCollector;
import storm.trident.tuple.TridentTuple;

public class FirstNSortedAgg extends BaseAggregator<PriorityQueue> {
	private int n;
	private String sortField;
	private boolean reverse;

	public FirstNSortedAgg(int n, String sortField, boolean reverse) {
		this.n = n;
		this.sortField = sortField;
		this.reverse = reverse;
	}

	@Override
	public PriorityQueue init(Object batchId, TridentCollector collector) {
		return null;
	}

	@Override
	public void aggregate(PriorityQueue val, TridentTuple tuple,
			TridentCollector collector) {
		// TODO Auto-generated method stub

	}

	@Override
	public void complete(PriorityQueue val, TridentCollector collector) {
		// TODO Auto-generated method stub

	}
}
