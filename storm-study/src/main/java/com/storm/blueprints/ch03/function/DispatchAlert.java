package com.storm.blueprints.ch03.function;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.storm.blueprints.ch03.filter.DiseaseFilter;

import storm.trident.operation.BaseFunction;
import storm.trident.operation.TridentCollector;
import storm.trident.tuple.TridentTuple;

public class DispatchAlert extends BaseFunction {
	private static final long serialVersionUID = 1L;
	private static final Logger LOG = LoggerFactory
			.getLogger(DiseaseFilter.class);

	@Override
	public void execute(TridentTuple tuple, TridentCollector collector) {
		String alert = (String) tuple.getValue(0);
		LOG.error("alert received [{}", alert);
		LOG.error("dispatch the national guard");
		System.exit(0);
	}
}