package com.storm.examples.madvirus.trident;

import java.util.Arrays;

import storm.trident.operation.BaseFunction;
import storm.trident.operation.TridentCollector;
import storm.trident.tuple.TridentTuple;

public class AddGroupingValueFunction extends BaseFunction {

	private static final long serialVersionUID = 1L;

	@Override
	public void execute(TridentTuple tuple, TridentCollector collector) {
		ShopLog shopLog = (ShopLog) tuple.getValueByField("shopLog");
		long time = shopLog.getTimeStamp() / (1000L * 60L);
		collector.emit(Arrays.<Object> asList(shopLog.getProduceId() + ":"
				+ time));
	}

}
