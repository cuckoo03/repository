package com.storm.blueprints.ch01;

import backtype.storm.Config;
import backtype.storm.LocalCluster;
import backtype.storm.StormSubmitter;
import backtype.storm.generated.AlreadyAliveException;
import backtype.storm.generated.InvalidTopologyException;
import backtype.storm.topology.TopologyBuilder;
import backtype.storm.tuple.Fields;
import backtype.storm.utils.Utils;

public class WordCountTopology {
	private static final String SENTENCE_SPOUT_ID = "sentence-spout";
	private static final String SPLIT_BOLT_ID = "split-bolt";
	private static final String COUNT_BOLT_ID = "count-bolt";
	private static final String REPORT_BOLT_ID = "report-bolt";
	private static final String TOPOLOGY_NAME = "word-count-topology";

	public static void main(String[] args) throws AlreadyAliveException,
			InvalidTopologyException {
		SentenceSpout spout = new SentenceSpout();
		SplitSentenceBolt splitBolt = new SplitSentenceBolt();
		WordCountBolt countBolt = new WordCountBolt();
		ReportBolt reportBolt = new ReportBolt();

		TopologyBuilder builder = new TopologyBuilder();

		builder.setSpout(SENTENCE_SPOUT_ID, spout);
//		builder.setBolt(SPLIT_BOLT_ID, splitBolt).shuffleGrouping(
//				SENTENCE_SPOUT_ID);
//		builder.setBolt(COUNT_BOLT_ID, countBolt).fieldsGrouping(SPLIT_BOLT_ID,
//				new Fields("word"));
//		builder.setBolt(REPORT_BOLT_ID, reportBolt).globalGrouping(
//				COUNT_BOLT_ID);

		Config config = new Config();

		if (args.length == 0) {
			LocalCluster cluster = new LocalCluster();
			cluster.submitTopology(TOPOLOGY_NAME, config,
					builder.createTopology());
			Utils.sleep(1000 * 60 * 10);
			cluster.killTopology(TOPOLOGY_NAME);
			cluster.shutdown();
		} else {
			StormSubmitter.submitTopology(args[0], config,
					builder.createTopology());
		}
	}
}