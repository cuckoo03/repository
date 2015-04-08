package com.storm.examples.madvirus.trident;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Random;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import backtype.storm.utils.Utils;
import storm.trident.operation.TridentCollector;
import storm.trident.spout.ITridentSpout.Emitter;
import storm.trident.topology.TransactionAttempt;

public class LogEmitter implements Emitter<Long> {
	private static final Logger LOG = LoggerFactory.getLogger(LogEmitter.class);

	@Override
	public void emitBatch(TransactionAttempt tx, Long coordinatorMeta,
			TridentCollector collector) {
		LOG.info("Emitter.emitBatch({}, {}, collector)", tx, coordinatorMeta);
		List<String> logs = getLogs(coordinatorMeta);
		for (String log : logs) {
			List<Object> oneTuple = Arrays.<Object> asList(log);
			collector.emit(oneTuple);
		}
	}

	private Random rand = new Random();
	private String[] arr = new String[] { "A", "B", "C", "D", "E" };

	private List<String> getLogs(Long coordinatorMeta) {
		List<String> logs = new ArrayList<>();

		int randNumber = rand.nextInt(5);
		for (int i = 0; i < randNumber; i++) {
			logs.add("ORDER:" + arr[i]);
		}
//		randNumber = rand.nextInt(5);
//		for (int i = 0; i < randNumber; i++) {
//			logs.add("ORDER:" + arr[i]);
//		}
		Utils.sleep(5000);
		return logs;
	}

	@Override
	public void success(TransactionAttempt tx) {
		LOG.info("Emitter.success({})", tx);
	}

	@Override
	public void close() {
		LOG.info("Emitter.close()");
	}
}