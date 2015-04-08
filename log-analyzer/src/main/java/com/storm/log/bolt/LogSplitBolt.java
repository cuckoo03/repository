package com.storm.log.bolt;

import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import backtype.storm.task.OutputCollector;
import backtype.storm.task.TopologyContext;
import backtype.storm.topology.OutputFieldsDeclarer;
import backtype.storm.topology.base.BaseRichBolt;
import backtype.storm.tuple.Fields;
import backtype.storm.tuple.Tuple;
import backtype.storm.tuple.Values;

import com.hadoop.log.RegExpUtil;
import com.storm.log.LogEvent;

/**
 * kafka에서 받은 로그를 정규식으로 나누어 객체로 전달
 * 
 * @author cuckoo03
 *
 */
public class LogSplitBolt extends BaseRichBolt {
	private static final long serialVersionUID = 1L;
	private static final Logger LOG = LoggerFactory
			.getLogger(LogSplitBolt.class);
	private OutputCollector collector;
	private static final Pattern p = Pattern.compile(RegExpUtil
			.getApacheLogRegex());

	@Override
	public void prepare(Map stormConf, TopologyContext context,
			OutputCollector collector) {
		this.collector = collector;
	}

	@Override
	public void execute(Tuple input) {
		String line = input.getString(0);
		Matcher matcher = p.matcher(line);
		if (!matcher.matches()) {
			return;
		}

		LogEvent event = new LogEvent();
		event.setLocalhost(matcher.group(1));
		event.setIdentd(matcher.group(2));
		event.setUserId(matcher.group(3));
		event.setRequestDate(matcher.group(4));
		event.setRequestInfo(matcher.group(5));
		event.setStatus(matcher.group(6));
		event.setSize(matcher.group(7));

		collector.emit(new Values(event.getLocalhost()));
	}

	@Override
	public void declareOutputFields(OutputFieldsDeclarer declarer) {
		declarer.declare(new Fields("localhost"));
	}
}