package com.storm.hadoop;

import org.apache.storm.hdfs.bolt.HdfsBolt;
import org.apache.storm.hdfs.bolt.format.DefaultFileNameFormat;
import org.apache.storm.hdfs.bolt.format.DelimitedRecordFormat;
import org.apache.storm.hdfs.bolt.format.FileNameFormat;
import org.apache.storm.hdfs.bolt.format.RecordFormat;
import org.apache.storm.hdfs.bolt.rotation.FileRotationPolicy;
import org.apache.storm.hdfs.bolt.rotation.FileSizeRotationPolicy;
import org.apache.storm.hdfs.bolt.rotation.FileSizeRotationPolicy.Units;
import org.apache.storm.hdfs.bolt.sync.CountSyncPolicy;
import org.apache.storm.hdfs.bolt.sync.SyncPolicy;

import backtype.storm.Config;
import backtype.storm.LocalCluster;
import backtype.storm.generated.AlreadyAliveException;
import backtype.storm.generated.InvalidTopologyException;
import backtype.storm.testing.TestWordSpout;
import backtype.storm.topology.TopologyBuilder;
import backtype.storm.utils.Utils;

import com.storm.common.StormClient;

public class StormHdfsTopology implements StormClient {
	private TopologyBuilder builder = new TopologyBuilder();
	private Config conf = new Config();
	private LocalCluster localCluster;

	public static void main(String[] args) throws AlreadyAliveException,
			InvalidTopologyException {
		StormHdfsTopology topology = new StormHdfsTopology();
		topology.createTopology();

		if (args.length != 1) {
			System.out.println("run cluster");
			topology.runCluster(null, null, null);

		} else if (args.length == 1) {
			System.out.println("run local");
			topology.runLocal(Integer.parseInt(args[0]));
		}
	}

	@Override
	public void createTopology() {
		// Use pipe as record boundary
		RecordFormat format = new DelimitedRecordFormat()
				.withFieldDelimiter("|");

		// Synchronize data buffer with the filesystem every 1000 tuples
		SyncPolicy syncPolicy = new CountSyncPolicy(1000);

		// Rotate data files when they reach five MB
		FileRotationPolicy rotationPolicy = new FileSizeRotationPolicy(5.0f,
				Units.MB);

		// Use default, Storm-generated file names
		// file naming:/foo/hdfs-bolt-2-0-111111111111.txt
		FileNameFormat fileNameFormat = new DefaultFileNameFormat()
				.withPath("/foo").withPrefix("server-").withExtension(".txt");

		// Instantiate the HdfsBolt
		HdfsBolt bolt = new HdfsBolt().withFsUrl("hdfs://master:9000")
				.withFileNameFormat(fileNameFormat).withRecordFormat(format)
				.withRotationPolicy(rotationPolicy).withSyncPolicy(syncPolicy);

		builder.setSpout("spout", new TestWordSpout());
		builder.setBolt("hdfs-bolt", bolt).shuffleGrouping("spout");
	}

	@Override
	public void runLocal(int runTime) {
		localCluster = new LocalCluster();
		localCluster.submitTopology("test", conf, builder.createTopology());
		if (runTime > 0) {
			Utils.sleep(runTime);
			shutdownLocal();
		}
	}

	@Override
	public void shutdownLocal() {
		if (localCluster != null) {
			localCluster.killTopology("test");
			localCluster.shutdown();
		}
	}

	@Override
	public void runCluster(String name, String redisHost, String redisPass)
			throws AlreadyAliveException, InvalidTopologyException {
		// TODO Auto-generated method stub

	}
}
