package client2

import org.apache.zookeeper.CreateMode
import org.apache.zookeeper.WatchedEvent
import org.apache.zookeeper.Watcher
import org.apache.zookeeper.ZooKeeper
import org.apache.zookeeper.ZooDefs.Ids

class ZNodeTest implements Watcher {
	private static final String ZK_HOST = "192.168.1.101:2181"
	private static final int ZK_SESSION_TIMEOUT = 6 * 1000
	private ZooKeeper zk
	public ZNodeTest() {
		zk = new ZooKeeper(ZK_HOST, ZK_SESSION_TIMEOUT, this)
	}
	@Override
	public void process(WatchedEvent event) {
		println "process, " + event.getType() + "," + event.getState()
	}
	private void execute() {
		String znode = "/servers/c2"
		if (zk.exists(znode, false) == null) {
			zk.create(znode, "c2".getBytes(), Ids.OPEN_ACL_UNSAFE,
					CreateMode.EPHEMERAL)
			println "Create node data:" + new String(zk.getData(znode, false, null))
		} else {
			println "Node exists:" + new String(zk.getData(znode, false, null))
		}
		zk.close()
		println "close"
	}
	static main(args) {
		ZNodeTest t = new ZNodeTest()
		t.execute()
	}
}
