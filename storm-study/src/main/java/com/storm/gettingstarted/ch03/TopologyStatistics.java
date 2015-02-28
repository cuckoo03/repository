package com.storm.gettingstarted.ch03;

import java.util.Iterator;

import org.apache.thrift7.TException;

import backtype.storm.generated.ClusterSummary;
import backtype.storm.generated.Nimbus.Client;
import backtype.storm.generated.TopologySummary;

public class TopologyStatistics {
	public void printTopologoyStatistics() {
		ThriftClient thriftClient = new ThriftClient();
		Client client = thriftClient.getClient();

		try {
			ClusterSummary clusterSummary = client.getClusterInfo();
			Iterator<TopologySummary> topologiesIter = clusterSummary
					.get_topologies_iterator();
			while (topologiesIter.hasNext()) {
				TopologySummary summary = topologiesIter.next();
				System.out.println("*************");
				System.out.println(summary.get_id());
			}
		} catch (TException e) {
			e.printStackTrace();
		}
	}
}
