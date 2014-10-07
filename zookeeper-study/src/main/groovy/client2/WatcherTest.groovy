package client2

import org.apache.zookeeper.WatchedEvent
import org.apache.zookeeper.Watcher
import org.apache.zookeeper.Watcher.Event.KeeperState;
import org.apache.zookeeper.ZooKeeper
import org.apache.zookeeper.Watcher.Event

/**
 * watcher timeout
 * @author cuckoo03
 *
 */
class WatcherTest implements Watcher {
	private static final String ZK_HOST = "192.168.1.101:2181"
	private static final int ZK_SESSION_TIMEOUT = 6 * 1000
	private ZooKeeper zk
	public WatcherTest() throws Exception {
		zk = new ZooKeeper(ZK_HOST, ZK_SESSION_TIMEOUT, this)
		SampleNodeWatcher watcher = new SampleNodeWatcher()
		zk.exists("/servers", watcher)
	}

	@Override
	public void process(WatchedEvent event) {
		println "process():" + event.getType() + ", state:" + event.getState()
		if (event.getType() == Event.EventType.None) {
			switch (event.getState()) {
				case KeeperState.SyncConnected:
					break
				case KeeperState.Disconnected:
					break
				case KeeperState.Expired:
					break
			}
		}
	}
	public void exec() {
		while (true) {
			try {
				sleep(5 * 1000)
				long sid = zk.getSessionId()
				byte[] passwd = zk.getSessionPasswd()
				zk.close()
				zk = new ZooKeeper(ZK_HOST, ZK_SESSION_TIMEOUT, this, sid, passwd)
			} catch (Exception e) {
				e.printStackTrace()
			}
		}
	}

	static main(args) throws Exception {
		WatcherTest test = new WatcherTest()
		test.exec()
	}
}
class SampleNodeWatcher implements Watcher {
	@Override
	public void process(WatchedEvent event) {
		"Watcher.process()"
		if (event.getType() == Event.EventType.NodeCreated) {
			println "Node created:$event.getPath()"
		} else if (event.getType() == Event.EventType.NodeDeleted) {
			println "Node deleted:$event.getPath()"
		} else if (event.getType() == Event.EventType.NodeDataChanged) {
			println "Node data Changed:$event.getPath()"
		}
	}
}