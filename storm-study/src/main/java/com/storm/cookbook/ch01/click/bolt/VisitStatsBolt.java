package com.storm.cookbook.ch01.click.bolt;

import java.util.Map;

import backtype.storm.task.OutputCollector;
import backtype.storm.task.TopologyContext;
import backtype.storm.topology.OutputFieldsDeclarer;
import backtype.storm.topology.base.BaseRichBolt;
import backtype.storm.tuple.Fields;
import backtype.storm.tuple.Tuple;
import backtype.storm.tuple.Values;

import com.storm.cookbook.ch01.click.constant.FieldsConstant;

public class VisitStatsBolt extends BaseRichBolt {
	private static final long serialVersionUID = 1L;
	private OutputCollector collector;
	private int total = 0;
	private int uniqueCount = 0;

	@Override
	public void prepare(Map stormConf, TopologyContext context,
			OutputCollector collector) {
		this.collector = collector;
	}

	@Override
	public void execute(Tuple input) {
		boolean unique = Boolean.parseBoolean(input
				.getStringByField(FieldsConstant.UNIQUE));
		total++;
		if (unique) {
			uniqueCount++;
		}
		System.out.println("total:" + total + ", unique:" + uniqueCount);
		collector.emit(new Values(total, uniqueCount));
	}

	@Override
	public void declareOutputFields(OutputFieldsDeclarer declarer) {
		declarer.declare(new Fields(FieldsConstant.TOTAL_COUNT,
				FieldsConstant.TOTAL_UNIQUE));
	}
}