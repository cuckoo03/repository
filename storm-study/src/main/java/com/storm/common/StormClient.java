package com.storm.common;

import backtype.storm.generated.AlreadyAliveException;
import backtype.storm.generated.InvalidTopologyException;

public interface StormClient {
	void createTopology();
	void runLocal(int runTime);
	void shutdownLocal();
	void runCluster(String name, String redisHost, String redisPass)
			throws AlreadyAliveException, InvalidTopologyException;
}