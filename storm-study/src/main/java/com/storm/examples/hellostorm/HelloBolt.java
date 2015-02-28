package com.storm.examples.hellostorm;

import backtype.storm.topology.BasicOutputCollector;
import backtype.storm.topology.OutputFieldsDeclarer;
import backtype.storm.topology.base.BaseBasicBolt;
import backtype.storm.tuple.Tuple;

public class HelloBolt extends BaseBasicBolt {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public void execute(Tuple input, BasicOutputCollector collector) {
		String value = input.getStringByField("say");
		System.out.println(value);
	}

	public void declareOutputFields(OutputFieldsDeclarer declarer) {
	}
}