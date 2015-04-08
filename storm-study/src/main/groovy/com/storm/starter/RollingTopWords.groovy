package com.storm.starter

import groovy.transform.TypeChecked
import storm.starter.tools.TestingWordSpout
import backtype.storm.Config
import backtype.storm.topology.TopologyBuilder
import backtype.storm.tuple.Fields

import com.storm.starter.bolt.IntermediateRankingBolt
import com.storm.starter.bolt.RollingCountBolt
import com.storm.starter.bolt.TotalRankingsBolt
import com.storm.starter.util.StormRunner

@TypeChecked
class RollingTopWords {
	private static final int DEFAULT_RUNTIME_IN_SECONDS = 60
	private static final int TOP_N = 3

	private final TopologyBuilder builder
	private final String topologyName
	private final Config topologyConfig
	private final int runtimeInSeconds = 600

	public RollingTopWords() {
		builder = new TopologyBuilder()
		topologyName = "slidingWindowCounts"
		topologyConfig = creteTopologyConfiguration()

		wireTopology()
	}
	public void run() {
		StormRunner.runTopologyLocally(builder.createTopology(), topologyName,
				topologyConfig, runtimeInSeconds)
	}
	private static Config creteTopologyConfiguration() {
		Config conf = new Config()
		conf.setDebug(false)
		return conf
	}
	private void wireTopology() {
		String spoutId = "wordGenerator"
		String counterId = "counter"
		String intermediateRankerId = "intermediateRanker"
		String totalRankerId = "finalRanker"
		builder.setSpout(spoutId, new TestingWordSpout())

		builder.setBolt(counterId, new RollingCountBolt(8, 4)).
				fieldsGrouping(spoutId, new Fields("word"))

		builder.setBolt(intermediateRankerId,
				new IntermediateRankingBolt(TOP_N)).
				fieldsGrouping(counterId, new Fields("obj"))

		builder.setBolt(totalRankerId, new TotalRankingsBolt(TOP_N)).
				globalGrouping(intermediateRankerId)
	}
	static main(args) {
		new RollingTopWords().run()
	}
}