package com.storm.blueprints.ch01;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import backtype.storm.task.OutputCollector;
import backtype.storm.task.TopologyContext;
import backtype.storm.topology.OutputFieldsDeclarer;
import backtype.storm.topology.base.BaseRichBolt;
import backtype.storm.tuple.Tuple;

public class ReportBolt extends BaseRichBolt {
	private Map<String, Long> counts = null;

	@Override
	public void prepare(Map config, TopologyContext context,
			OutputCollector collector) {
		this.counts = new HashMap<>();
	}

	@Override
	public void execute(Tuple tuple) {
		String word = tuple.getStringByField("word");
		Long count = tuple.getLongByField("count");
		this.counts.put(word, count);
	}

	@Override
	public void declareOutputFields(OutputFieldsDeclarer declarer) {
	}

	@Override
	public void cleanup() {
		System.out.println("final counts");
		List<String> keys = new ArrayList<>();
		keys.addAll(this.counts.keySet());
		Collections.sort(keys);
		for (String key : keys) {
			System.out.println(key + ":" + this.counts.get(key));
		}
		System.out.println("end");
	}
}
