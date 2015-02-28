package com.storm.examples.trident;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import storm.trident.operation.BaseFilter;
import storm.trident.tuple.TridentTuple;

public class OrderLogFilter extends BaseFilter {
	private static final long serialVersionUID = 1L;
	private static final Logger LOG = LoggerFactory
			.getLogger(OrderLogFilter.class);

	@Override
	public boolean isKeep(TridentTuple tuple) {
		String logString = tuple.getStringByField("logString");
		boolean pass = logString.startsWith("ORDER");
		if (!pass) {
			LOG.info("OrderLogFilter filtered out{}", logString);
		}
		return pass;
	}
}