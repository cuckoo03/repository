package com.storm.starter.bolt


import java.util.Map;

import groovy.transform.TypeChecked

import org.slf4j.Logger

import storm.starter.tools.Rankings
import storm.starter.util.TupleHelpers
import backtype.storm.Config
import backtype.storm.topology.BasicOutputCollector
import backtype.storm.topology.OutputFieldsDeclarer
import backtype.storm.topology.base.BaseBasicBolt
import backtype.storm.tuple.Fields
import backtype.storm.tuple.Tuple
import backtype.storm.tuple.Values

@TypeChecked
abstract class AbstractRankerBolt extends BaseBasicBolt {
	private static final int DEFAULT_EMIT_FREQUENCY_IN_SECONDS = 2
	private static final int DEFAULT_COUNT = 10

	private final int emitFrequencyInSeconds
	private final int count
	private final Rankings rankings

	public AbstractRankerBolt() {
		this(DEFAULT_COUNT, DEFAULT_EMIT_FREQUENCY_IN_SECONDS)
	}

	public AbstractRankerBolt(int topN) {
		this(topN, DEFAULT_EMIT_FREQUENCY_IN_SECONDS)
	}

	public AbstractRankerBolt(int topN, int emitFrequencyInSeconds) {
		if (topN < 1) {
			throw new IllegalArgumentException("topN must be >= 1(you requested $topN")
		}
		count = topN
		this.emitFrequencyInSeconds = emitFrequencyInSeconds
		rankings = new Rankings(count)
	}

	protected Rankings getRankings() {
		return rankings
	}

	@Override
	public void execute(Tuple tuple, BasicOutputCollector collector) {
		if (TupleHelpers.isTickTuple(tuple)) {
			println "Received tick tuple, triggering emit of currrent rankings"
			emitRankings(collector)
		} else {
			updateRankingsWithTuple(tuple)
		}
	}

	abstract void updateRankingsWithTuple(Tuple tuple);

	private void emitRankings(BasicOutputCollector collector) {
		collector.emit(new Values(rankings.copy()))
	}

	@Override
	public void declareOutputFields(OutputFieldsDeclarer declarer) {
		declarer.declare(new Fields("rankings"))
	}

	@Override
	public Map<String, Object> getComponentConfiguration() {
		Map<String, Object> conf = new HashMap<String, Object>()
		conf.put(Config.TOPOLOGY_TICK_TUPLE_FREQ_SECS, emitFrequencyInSeconds)
		return conf
	}
}