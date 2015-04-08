package com.storm.log;

import java.util.Map;

import backtype.storm.spout.SpoutOutputCollector;
import backtype.storm.task.TopologyContext;
import backtype.storm.topology.OutputFieldsDeclarer;
import backtype.storm.topology.base.BaseRichSpout;
import backtype.storm.tuple.Fields;
import backtype.storm.tuple.Values;
import backtype.storm.utils.Utils;

public class MockSpout extends BaseRichSpout {
	private static final long serialVersionUID = 1L;
	private SpoutOutputCollector collector;
	private String[] array = new String[] {
			"127.0.0.1 - - [07/Mar/2014:17:04:55 +0900] \"POST /goodnight/admin/hotel_open HTTP/1.1\" 200 20514",
			"127.0.0.1 - - [07/Mar/2014:17:04:58 +0900] \"POST /goodnight/admin/sale HTTP/1.1\" 200 70894" };
	private int count = 0;

	@Override
	public void open(Map conf, TopologyContext context,
			SpoutOutputCollector collector) {
		this.collector = collector;
	}

	@Override
	public void nextTuple() {
		count++;
		if (count > 1) {
			return;
		}
		collector.emit(new Values(array[0]));
		Utils.sleep(1000);
	}

	@Override
	public void declareOutputFields(OutputFieldsDeclarer declarer) {
		declarer.declare(new Fields("line"));
	}
}