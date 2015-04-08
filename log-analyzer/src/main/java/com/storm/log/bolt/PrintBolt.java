package com.storm.log.bolt;

import java.util.HashMap;
import java.util.Map;

import backtype.storm.task.OutputCollector;
import backtype.storm.task.TopologyContext;
import backtype.storm.topology.OutputFieldsDeclarer;
import backtype.storm.topology.base.BaseRichBolt;
import backtype.storm.tuple.Tuple;

public class PrintBolt extends BaseRichBolt {
	private static final long serialVersionUID = 1L;
	private Map<String, Integer> countMap = new HashMap<>();

	@Override
	public void prepare(Map stormConf, TopologyContext context,
			OutputCollector collector) {
	}

	@Override
	public void execute(Tuple input) {
		String localhost = input.getStringByField("localhost");
		Integer count = input.getIntegerByField("count");
		System.out.println(localhost + "," + count);
	}

	@Override
	public void declareOutputFields(OutputFieldsDeclarer declarer) {
	}
}