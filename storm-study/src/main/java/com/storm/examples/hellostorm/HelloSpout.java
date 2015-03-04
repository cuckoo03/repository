package com.storm.examples.hellostorm;

import java.util.Map;

import backtype.storm.spout.SpoutOutputCollector;
import backtype.storm.task.TopologyContext;
import backtype.storm.topology.OutputFieldsDeclarer;
import backtype.storm.topology.base.BaseRichSpout;
import backtype.storm.tuple.Fields;
import backtype.storm.tuple.Values;

public class HelloSpout extends BaseRichSpout {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private SpoutOutputCollector collector;
	private String[] strArr = { 
			"0 1 2 3 4 5 6 7 8 9",
			"1 2 3 4 5 6 7 8 9",
			"2 3 4 5 6 7 8 9",
			"3 4 5 6 7 8 9",
			"4 5 6 7 8 9",
			"5 6 7 8 9",
			"6 7 8 9",
			"7 8 9",
			"8 9",
			"9"
			};
	private int index = 0;

	public void open(Map conf, TopologyContext context,
			SpoutOutputCollector collector) {
		this.collector = collector;
	}

	public void nextTuple() {
		if (index < strArr.length) {
			String[] split = strArr[index].split(" ");
			for (int i = 0; i < split.length; i++) {
				this.collector.emit(new Values(split[i]));
			}
			index++;
		}
	}

	public void declareOutputFields(OutputFieldsDeclarer declarer) {
		declarer.declare(new Fields("word"));
	}
}