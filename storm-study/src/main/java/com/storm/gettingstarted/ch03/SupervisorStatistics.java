package com.storm.gettingstarted.ch03;

import java.util.Iterator;

import org.apache.thrift7.TException;

import backtype.storm.generated.ClusterSummary;
import backtype.storm.generated.Nimbus.Client;
import backtype.storm.generated.SupervisorSummary;

public class SupervisorStatistics {
	public void printSupervisorStatistics() {
		ThriftClient thriftClient = new ThriftClient();
		Client client = thriftClient.getClient();

		try {
			ClusterSummary clusterSummary = client.getClusterInfo();
			Iterator<SupervisorSummary> supervisorsIter = clusterSummary
					.get_supervisors_iterator();

			while (supervisorsIter.hasNext()) {
				SupervisorSummary summary = supervisorsIter.next();
				System.out.println("****************");
				System.out.println("host ip:" + summary.get_host());
				System.out.println("number of used workers:"
						+ summary.get_num_used_workers());
			}
		} catch (TException e) {
			e.printStackTrace();
			throw new RuntimeException(
					"Error occurred while getting cluster info");
		}
	}
}