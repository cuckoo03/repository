package com.storm.blueprints.ch03.spout;

import java.util.Map;

import storm.trident.spout.ITridentSpout;
import backtype.storm.task.TopologyContext;
import backtype.storm.tuple.Fields;

import com.storm.blueprints.ch03.coordinator.DefaultCoodinator;
import com.storm.blueprints.ch03.emitter.DiagnosisEventEmitter;

public class DiagnosisEventSpout implements ITridentSpout<Long> {
	private static final long serialVersionUID = 1L;
	private BatchCoordinator<Long> coordinator = new DefaultCoodinator();
	private Emitter<Long> emitter = new DiagnosisEventEmitter();

	@Override
	public BatchCoordinator<Long> getCoordinator(String txStateId, Map conf,
			TopologyContext context) {
		return coordinator;
	}

	@Override
	public Emitter<Long> getEmitter(String txStateId, Map conf,
			TopologyContext context) {
		return emitter;
	}

	@Override
	public Map getComponentConfiguration() {
		return null;
	}

	@Override
	public Fields getOutputFields() {
		return new Fields("event");
	}
}