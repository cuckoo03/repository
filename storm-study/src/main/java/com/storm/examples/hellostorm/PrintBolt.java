package com.storm.examples.hellostorm;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import backtype.storm.task.OutputCollector;
import backtype.storm.task.TopologyContext;
import backtype.storm.topology.OutputFieldsDeclarer;
import backtype.storm.topology.base.BaseRichBolt;
import backtype.storm.tuple.Tuple;

public class PrintBolt extends BaseRichBolt {
	Map<String, Integer> wordMap;

	@Override
	public void prepare(Map stormConf, TopologyContext context,
			OutputCollector collector) {
		wordMap = new HashMap<>();
	}

	@Override
	public void execute(Tuple input) {
		String word = input.getStringByField("word");
		Integer count = input.getIntegerByField("count");
		this.wordMap.put(word, count);

	}

	@Override
	public void declareOutputFields(OutputFieldsDeclarer declarer) {

	}

	@Override
	public void cleanup() {
		List<String> list = new ArrayList<>();
		list.addAll(wordMap.keySet());
		System.out.println("******************************");
		for (String key : list) {
			System.out.println(key + "," + wordMap.get(key));
		}
		System.out.println("******************************");
	}
}
