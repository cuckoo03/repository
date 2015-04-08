package com.storm.hadoop.spout;

import java.util.Map;
import java.util.Random;
import java.util.UUID;

import backtype.storm.spout.SpoutOutputCollector;
import backtype.storm.task.TopologyContext;
import backtype.storm.topology.OutputFieldsDeclarer;
import backtype.storm.topology.base.BaseRichSpout;
import backtype.storm.tuple.Fields;
import backtype.storm.tuple.Values;

public class WordSpout extends BaseRichSpout {
	private static final long serialVersionUID = 1L;
	public static final String[] words = new String[] { "a", "b", "c", "d", "e" };
	private boolean isDistributed;
	private SpoutOutputCollector collector;

	public WordSpout() {
		this(true);
	}

	public WordSpout(boolean isDistributed) {
		this.isDistributed = isDistributed;
	}

	@Override
	public void open(Map conf, TopologyContext context,
			SpoutOutputCollector collector) {
		this.collector = collector;
	}

	@Override
	public void nextTuple() {
		final Random random = new Random();
		final String word = words[random.nextInt(words.length)];
		this.collector.emit(new Values(word, UUID.randomUUID()));
		Thread.yield();
	}

	@Override
	public void declareOutputFields(OutputFieldsDeclarer declarer) {
		declarer.declare(new Fields("word", "uuid"));
	}
}