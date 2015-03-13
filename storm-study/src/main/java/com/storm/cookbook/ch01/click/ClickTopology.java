package com.storm.cookbook.ch01.click;

import backtype.storm.Config;
import backtype.storm.LocalCluster;
import backtype.storm.StormSubmitter;
import backtype.storm.generated.AlreadyAliveException;
import backtype.storm.generated.InvalidTopologyException;
import backtype.storm.topology.TopologyBuilder;
import backtype.storm.tuple.Fields;
import backtype.storm.utils.Utils;

import com.storm.common.Conf;
import com.storm.common.StormClient;
import com.storm.cookbook.ch01.click.bolt.GeoStatsBolt;
import com.storm.cookbook.ch01.click.bolt.GeographyBolt;
import com.storm.cookbook.ch01.click.bolt.RepeatVisitBolt;
import com.storm.cookbook.ch01.click.bolt.VisitStatsBolt;
import com.storm.cookbook.ch01.click.constant.FieldsConstant;
import com.storm.cookbook.ch01.click.spout.ClickSpout;

/**
 * 사용자 총 카운트와 유니크한 카운트 수를 세는 프로그램
 * 
 * @author cuckoo03
 *
 */
public class ClickTopology implements StormClient {
	private static final String CLICK_SPOUT_ID = "click-spout";
	private static final String REPEAT_BOLT_ID = "repeat-bolt";
	private static final String GEOGRAPHY_BOLT_ID = "geography-bolt";
	private static final String TOTAL_STATS_BOLT_ID = "total-stats-bolt";
	private static final String GEO_STATS_BOLT_ID = "geo-stats-bolt";

	private TopologyBuilder builder = new TopologyBuilder();
	private Config conf = new Config();
	private LocalCluster localCluster;

	@Override
	public void createTopology() {
		builder.setSpout(CLICK_SPOUT_ID, new ClickSpout(), 2);
		// 첫번째 레이어
		builder.setBolt(REPEAT_BOLT_ID, new RepeatVisitBolt(), 2)
				.shuffleGrouping(CLICK_SPOUT_ID);
		// builder.setBolt(GEOGRAPHY_BOLT_ID,
		// new GeographyBolt(new HttpIpResolver()), 10).shuffleGrouping(
		// CLICK_SPOUT_ID);
		// 두번째 레이어
		builder.setBolt(TOTAL_STATS_BOLT_ID, new VisitStatsBolt(), 1)
				.globalGrouping(REPEAT_BOLT_ID);
		// builder.setBolt(GEO_STATS_BOLT_ID, new GeoStatsBolt(), 10)
		// .fieldsGrouping(GEOGRAPHY_BOLT_ID,
		// new Fields(FieldsConstant.COUNTRY));
	}

	@Override
	public void runLocal(int runTime) {
		conf.setDebug(true);
		conf.put(Conf.REDIS_HOST_KEY, "192.168.1.101");
		conf.put(Conf.REDIS_PORT_KEY, "6300");
		conf.put(Conf.REDIS_PASS_KEY, "12341234");
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
//		conf.setNumWorkers(20);
		conf.put(Conf.REDIS_HOST_KEY, redisHost);
		conf.put(Conf.REDIS_PORT_KEY, "6300");
		conf.put(Conf.REDIS_PASS_KEY, redisPass);
		StormSubmitter.submitTopology(name, conf, builder.createTopology());
	}

	public static void main(String[] args) throws AlreadyAliveException,
			InvalidTopologyException {

		ClickTopology topology = new ClickTopology();
		topology.createTopology();

		if (args.length > 1) {
			System.out.println("run cluster");
			topology.runCluster(args[0], args[1], args[2]);
		} else if (args.length == 1) {
			System.out.println("run local");
			topology.runLocal(Integer.parseInt(args[0]));
		}
	}
}