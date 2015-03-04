package com.storm.blueprints.ch03.emitter;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import scala.actors.threadpool.AtomicInteger;
import storm.trident.operation.TridentCollector;
import storm.trident.spout.ITridentSpout.Emitter;
import storm.trident.topology.TransactionAttempt;

import com.storm.blueprints.ch03.DiagnosisEvent;
import com.storm.blueprints.ch03.state.OutbreakTrendBackingMap;

public class DiagnosisEventEmitter implements Emitter<Long>, Serializable {
	private static final long serialVersionUID = 1L;
	private static final Logger LOG = LoggerFactory
			.getLogger(OutbreakTrendBackingMap.class);
	private AtomicInteger successfulTransactions = new AtomicInteger();
	private Random rand = new Random();
	private boolean first = false;

	@Override
	public void emitBatch(TransactionAttempt tx, Long coordinatorMeta,
			TridentCollector collector) {
		if (!first) {
			first = true;
		} else {
			return;
		}
		for (int i = 0; i < 10; i++) {
			List<Object> events = new ArrayList<>();
			double lat = new Double(-30 + Math.random() * 75);
			double lng = new Double(-20 + Math.random() * 70);
			long time = System.currentTimeMillis();
			String diag = new Integer(320 + rand.nextInt(10)).toString();
			DiagnosisEvent event = new DiagnosisEvent(lat, lng, time, diag);
			events.add(event);
			collector.emit(events);
		}
	}

	@Override
	public void success(TransactionAttempt tx) {
		successfulTransactions.incrementAndGet();
	}

	@Override
	public void close() {
	}
}