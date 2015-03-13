package com.storm.cookbook.ch01.click.bolt;

import java.util.Map;

import org.json.simple.JSONObject;

import backtype.storm.task.OutputCollector;
import backtype.storm.task.TopologyContext;
import backtype.storm.topology.OutputFieldsDeclarer;
import backtype.storm.topology.base.BaseRichBolt;
import backtype.storm.tuple.Fields;
import backtype.storm.tuple.Tuple;
import backtype.storm.tuple.Values;

import com.storm.cookbook.ch01.click.HttpIpResolver;
import com.storm.cookbook.ch01.click.constant.FieldsConstant;

public class GeographyBolt extends BaseRichBolt {
	private static final long serialVersionUID = 1L;
	private HttpIpResolver resolver;
	private OutputCollector collector;

	public GeographyBolt(HttpIpResolver httpIpResolver) {
		this.resolver = httpIpResolver;
	}

	@Override
	public void prepare(Map stormConf, TopologyContext context,
			OutputCollector collector) {
		this.collector = collector;
	}

	@Override
	public void execute(Tuple input) {
		String ip = input.getStringByField(FieldsConstant.IP);
		JSONObject json = resolver.resolveIp(ip);
		String city = (String) json.get(FieldsConstant.CITY);
		String country = (String) json.get(FieldsConstant.COUNTRY);
		collector.emit(new Values(country, city));
	}

	@Override
	public void declareOutputFields(OutputFieldsDeclarer declarer) {
		declarer.declare(new Fields(FieldsConstant.COUNTRY, FieldsConstant.CITY));
	}
}