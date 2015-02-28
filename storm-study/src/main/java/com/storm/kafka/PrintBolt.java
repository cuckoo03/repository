package com.storm.kafka;

import backtype.storm.topology.BasicOutputCollector;
import backtype.storm.topology.OutputFieldsDeclarer;
import backtype.storm.topology.base.BaseBasicBolt;
import backtype.storm.tuple.Tuple;

public class PrintBolt extends BaseBasicBolt {
	private static final long serialVersionUID = 1L;

	@Override
	public void execute(Tuple input, BasicOutputCollector collector) {
		String sentence = input.getString(0);
		System.out.println("Received sentence:" + sentence);
	}

	@Override
	public void declareOutputFields(OutputFieldsDeclarer declarer) {
	}
}