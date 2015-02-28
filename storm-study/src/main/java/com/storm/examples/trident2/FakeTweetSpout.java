package com.storm.examples.trident2;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;

import storm.trident.operation.TridentCollector;
import storm.trident.spout.IBatchSpout;
import backtype.storm.task.TopologyContext;
import backtype.storm.tuple.Fields;
import backtype.storm.tuple.Values;
import backtype.storm.utils.Utils;

public class FakeTweetSpout implements IBatchSpout {
	private static final long serialVersionUID = 1L;
	private int batchsize;
	private Map<Long, List<List<Object>>> batchesMap = new HashMap<>();
	private static final Map<Integer, String> TWEET_MAP = new HashMap<>();
	static {
		TWEET_MAP.put(0, "A");
		TWEET_MAP.put(1, "#FIFA B");
		TWEET_MAP.put(2, "#FIFA C");
	}

	private static final Map<Integer, String> COUNTRY_MAP = new HashMap<>();
	static {
		COUNTRY_MAP.put(0, "a");
		COUNTRY_MAP.put(1, "b");
		COUNTRY_MAP.put(2, "c");
	}

	public FakeTweetSpout(int batchSize) {
		this.batchsize = batchSize;
	}

	@Override
	public void ack(long batchId) {
		this.batchesMap.remove(batchId);
	}

	@Override
	public void close() {
	}

	@Override
	public void emitBatch(long batchId, TridentCollector collector) {
		List<List<Object>> batches = this.batchesMap.get(batchesMap);
		if (batches == null) {
			batches = new ArrayList<List<Object>>();
			for (int i = 0; i < this.batchsize; i++) {
				batches.add(this.recordGenerator());
			}
			this.batchesMap.put(batchId, batches);
		}
		for (List<Object> list : batches) {
			collector.emit(list);
		}
		Utils.sleep(10000);

	}

	private List<Object> recordGenerator() {
		final Random random = new Random();
		int randNumber = random.nextInt(3);
		int randNumber2 = random.nextInt(3);
		return new Values(TWEET_MAP.get(randNumber),
				COUNTRY_MAP.get(randNumber2));
	}

	@Override
	public void open(Map conf, TopologyContext context) {
	}

	@Override
	public Map getComponentConfiguration() {
		return null;
	}

	@Override
	public Fields getOutputFields() {
		return new Fields("text", "Country");
	}
}