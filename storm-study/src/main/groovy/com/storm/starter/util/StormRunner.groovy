package com.storm.starter.util

import groovy.transform.TypeChecked;
import backtype.storm.Config
import backtype.storm.LocalCluster
import backtype.storm.generated.StormTopology
import backtype.storm.utils.Utils

@TypeChecked
final class StormRunner {
	private static final int MILLIS_IN_SEC = 1000
	private StormRunner() {}
	public static void runTopologyLocally(StormTopology topology,
			String topologyName, Config conf, int runtimeInSeconds) {
		LocalCluster cluster = new LocalCluster()
		cluster.submitTopology(topologyName, conf, topology)
		Utils.sleep( runtimeInSeconds * MILLIS_IN_SEC)
		cluster.killTopology(topologyName)
		cluster.shutdown()
	}
}