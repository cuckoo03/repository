package com.storm.blueprints.ch01;

import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

import backtype.storm.spout.SpoutOutputCollector;
import backtype.storm.task.TopologyContext;
import backtype.storm.topology.OutputFieldsDeclarer;
import backtype.storm.topology.base.BaseRichSpout;
import backtype.storm.tuple.Fields;
import backtype.storm.tuple.Values;
import backtype.storm.utils.Utils;

public class SentenceSpout extends BaseRichSpout {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private ConcurrentHashMap<UUID, Values> pending;
	private SpoutOutputCollector collector;
	private String[] sentences = { "my dog has fleas",
			"i like dog ate my homework" };

	private int index = 0;

	@Override
	public void declareOutputFields(OutputFieldsDeclarer declarer) {
		declarer.declare(new Fields("sentence"));
		this.pending = new ConcurrentHashMap<UUID, Values>();
	}

	@Override
	public void open(Map conf, TopologyContext context,
			SpoutOutputCollector collector) {
		this.collector = collector;
	}

	@Override
	public void nextTuple() {
		// nextTuple()는 계속 호출되므로 종료될수 있는 코드가 있어야 한다.
		if (index < sentences.length) {
			Values values = new Values(sentences[index]);
			UUID msgId = UUID.randomUUID();
			this.pending.put(msgId, values);
			this.collector.emit(values, msgId);
			index++;
		}
		Utils.sleep(1000);
	}
	@Override
	public void ack(Object msgId) {
		this.pending.remove(msgId);
	}
	@Override
	public void fail(Object msgId) {
		this.collector.emit(this.pending.get(msgId), msgId);
	}
}