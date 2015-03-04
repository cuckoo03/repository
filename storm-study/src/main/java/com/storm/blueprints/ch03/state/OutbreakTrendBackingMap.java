package com.storm.blueprints.ch03.state;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import storm.trident.state.map.IBackingMap;

public class OutbreakTrendBackingMap implements IBackingMap<Long> {
	private static final Logger LOG = LoggerFactory
			.getLogger(OutbreakTrendBackingMap.class);
	private Map<String, Long> storage = new ConcurrentHashMap<>();

	// [[NYC:325:395962], [NYC:320:395962], [NYC:328:395962], [NYC:327:395962],
	// [NYC:322:395962], [NYC:321:395962]]
	@Override
	public List<Long> multiGet(List<List<Object>> keys) {
		List<Long> values = new ArrayList<>();
		for (List<Object> key : keys) {
			Long value = storage.get(key.get(0));
			if (value == null) {
				values.add(new Long(0));
			} else {
				values.add(value);
			}
		}
		return values;
	}

	// [1, 3, 1, 1, 2, 2]
	@Override
	public void multiPut(List<List<Object>> keys, List<Long> vals) {
		for (int i = 0; i < keys.size(); i++) {
			LOG.info("persisting [{}]==>[{}]", keys.get(i).get(0), vals.get(i));
			storage.put((String) keys.get(i).get(0), vals.get(i));
		}
	}
}