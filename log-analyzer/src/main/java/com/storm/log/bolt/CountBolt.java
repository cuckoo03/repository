package com.storm.log.bolt;

import java.util.HashMap;
import java.util.Map;

import backtype.storm.task.OutputCollector;
import backtype.storm.task.TopologyContext;
import backtype.storm.topology.OutputFieldsDeclarer;
import backtype.storm.topology.base.BaseRichBolt;
import backtype.storm.tuple.Fields;
import backtype.storm.tuple.Tuple;
import backtype.storm.tuple.Values;

public class CountBolt extends BaseRichBolt {
	private static final long serialVersionUID = 1L;
	private Map<String, Integer> countMap = new HashMap<>();
	private OutputCollector collector;

	@Override
	public void prepare(Map stormConf, TopologyContext context,
			OutputCollector collector) {
		this.collector = collector;
	}

	@Override
	public void execute(Tuple input) {
		String key = input.getStringByField("localhost");
		Integer count = countMap.get(key);
		if (count == null) {
			count = 0;
		} else {
			count++;
		}
		countMap.put(key, count);
		collector.emit(new Values(key, count));
	}

	@Override
	public void declareOutputFields(OutputFieldsDeclarer declarer) {
		declarer.declare(new Fields("localhost", "count"));
	}
}