package com.zookeeper.configuration;

import java.io.IOException;

import org.apache.zookeeper.CreateMode;
import org.apache.zookeeper.KeeperException;
import org.apache.zookeeper.WatchedEvent;
import org.apache.zookeeper.Watcher;
import org.apache.zookeeper.ZooKeeper;
import org.apache.zookeeper.ZooDefs.Ids;

public class ConfigurationManager implements Watcher {
	private static final String HELLO_SERVICE_PATH = "/hello_service";
	private static final String CONF_PATH = HELLO_SERVICE_PATH + "/conf";
	private ZooKeeper zk;

	public ConfigurationManager() throws IOException {
		zk = new ZooKeeper("192.168.1.101:2181", 10 * 1000, this);
	}

	@Override
	public void process(WatchedEvent event) {

	}

	public void setConfValue(String key, String value) throws KeeperException,
			InterruptedException {
		String confPath = CONF_PATH + "/" + key;
		if (zk.exists(confPath, false) == null) {
			zk.create(confPath, value.getBytes(), Ids.OPEN_ACL_UNSAFE,
					CreateMode.PERSISTENT);
		} else {
			zk.setData(confPath, value.getBytes(), -1);
			System.out.println("Data changed:" + confPath + "," + value);
		}
	}

	public static void main(String[] args) throws Exception {
		args = new String[] { "k", "v" };
		if (args.length < 2) {
			System.out
					.println("Usage: java ConfigurationManager <key> <value>");
			System.exit(0);
		}
		new ConfigurationManager().setConfValue(args[0], args[1]);
	}
}