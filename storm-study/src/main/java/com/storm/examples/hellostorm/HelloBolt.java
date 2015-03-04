package com.storm.examples.hellostorm;

import java.util.HashMap;
import java.util.Map;

import backtype.storm.task.OutputCollector;
import backtype.storm.task.TopologyContext;
import backtype.storm.topology.OutputFieldsDeclarer;
import backtype.storm.topology.base.BaseRichBolt;
import backtype.storm.tuple.Fields;
import backtype.storm.tuple.Tuple;
import backtype.storm.tuple.Values;

public class HelloBolt extends BaseRichBolt {
	private static final long serialVersionUID = 1L;
	private Map<String, Integer> wordMap = new HashMap<>();
	private OutputCollector collector;

	@Override
	public void prepare(Map config, TopologyContext context,
			OutputCollector collector) {
		this.collector = collector;
	}

	@Override
	public void execute(Tuple input) {
		String value = input.getStringByField("word");
		Integer totalCount = wordMap.get(value);
		if (totalCount == null) {
			totalCount = 0;
		}
		totalCount++;
		wordMap.put(value, totalCount);
		collector.emit(new Values(value, totalCount));
	}

	public void declareOutputFields(OutputFieldsDeclarer declarer) {
		declarer.declare(new Fields("word", "count"));
	}
}