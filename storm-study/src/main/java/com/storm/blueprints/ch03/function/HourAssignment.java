package com.storm.blueprints.ch03.function;

import java.util.ArrayList;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.storm.blueprints.ch03.DiagnosisEvent;

import storm.trident.operation.BaseFunction;
import storm.trident.operation.TridentCollector;
import storm.trident.tuple.TridentTuple;

//33996 [Thread-24-b-2] INFO  com.storm.blueprints.ch03.function.CityAssignment - Key=NYC:320:395962
//36110 [Thread-24-b-2] INFO  com.storm.blueprints.ch03.function.CityAssignment - Key=NYC:322:395962
//36660 [Thread-24-b-2] INFO  com.storm.blueprints.ch03.function.CityAssignment - Key=NYC:325:395962
public class HourAssignment extends BaseFunction {
	private static final long serialVersionUID = 1L;
	private static final Logger LOG = LoggerFactory
			.getLogger(CityAssignment.class);

	@Override
	public void execute(TridentTuple tuple, TridentCollector collector) {
		DiagnosisEvent diagnosis = (DiagnosisEvent) tuple.getValue(0);
		String city = (String) tuple.getValue(1);

		long timestamp = diagnosis.getTime();
		long hourSinceEpoch = timestamp / 1000 / 60 / 60;

		String key = city + ":" + diagnosis.getDiag() + ":" + hourSinceEpoch;
		LOG.info("Key={}", key);

		List<Object> values = new ArrayList<>();
		values.add(hourSinceEpoch);
		values.add(key);
		collector.emit(values);
	}
}