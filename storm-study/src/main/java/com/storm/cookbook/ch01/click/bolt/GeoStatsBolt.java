package com.storm.cookbook.ch01.click.bolt;

import java.util.HashMap;
import java.util.Map;

import backtype.storm.task.OutputCollector;
import backtype.storm.task.TopologyContext;
import backtype.storm.topology.OutputFieldsDeclarer;
import backtype.storm.topology.base.BaseRichBolt;
import backtype.storm.tuple.Tuple;
import backtype.storm.tuple.Values;

import com.storm.cookbook.ch01.click.constant.FieldsConstant;

public class GeoStatsBolt extends BaseRichBolt {
	private static final long serialVersionUID = 1L;
	private OutputCollector collector;
	private Map<String, CountryStats> stats = new HashMap<>();

	@Override
	public void prepare(Map stormConf, TopologyContext context,
			OutputCollector collector) {
		this.collector = collector;
	}

	@Override
	public void execute(Tuple input) {
		String country = input.getStringByField(FieldsConstant.COUNTRY);
		String city = input.getStringByField(FieldsConstant.CITY);
		if (!stats.containsKey(country)) {
			stats.put(country, new CountryStats(country));
		}
		stats.get(country).cityFound(city);
		collector.emit(new Values(country,
				stats.get(country).getCountryTotal(), city, stats.get(city)
						.getCityTotal(city)));
	}

	@Override
	public void declareOutputFields(OutputFieldsDeclarer declarer) {
	}
}