package com.storm.examples.madvirus.trident;

import java.util.Map;

import storm.trident.spout.ITridentSpout;
import backtype.storm.task.TopologyContext;
import backtype.storm.tuple.Fields;

public class LogSpout implements ITridentSpout<Long>{


	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	@Override
	public BatchCoordinator<Long> getCoordinator(
			String txStateId, Map conf, TopologyContext context) {
		return new LogBatchCoordinator();
	}

	@Override
	public Emitter<Long> getEmitter(
			String txStateId, Map conf, TopologyContext context) {
		return new LogEmitter();
	}

	@Override
	public Map getComponentConfiguration() {
		return null;
	}

	@Override
	public Fields getOutputFields() {
		return new Fields("logString");
	}
}