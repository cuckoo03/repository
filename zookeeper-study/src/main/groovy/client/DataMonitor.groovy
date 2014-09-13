package client

import org.apache.zookeeper.WatchedEvent
import org.apache.zookeeper.Watcher
import org.apache.zookeeper.ZooKeeper
import org.apache.zookeeper.AsyncCallback.StatCallback
import org.apache.zookeeper.KeeperException.Code
import org.apache.zookeeper.Watcher.Event
import org.apache.zookeeper.Watcher.Event.KeeperState
import org.apache.zookeeper.data.Stat

class DataMonitor implements Watcher, StatCallback {
	private boolean dead
	private ZooKeeper zk
	private String znode
	private Watcher chainWatcher
	private DataMonitorListener listener
	private byte[] prevData

	public DataMonitor(ZooKeeper zk, String znode, Watcher chainWatcher,
	DataMonitorListener listener) {
		this.zk = zk
		this.znode = znode
		this.chainWatcher = chainWatcher
		this.listener = listener

		zk.exists(znode, true, this, null)
	}
	@Override
	public void processResult(int rc, String path, Object ctx, Stat stat) {
		println "DataMonitor processResult()"
		boolean exists
		switch(rc) {
			case Code.OK:
				exists = true
				break
			case Code.NONODE:
				exists = false
				break
			case Code.SESSIONEXPIRED:
				break
			case Code.NOAUTH:
				dead = true
				listener.closing(rc)
				return
			default:
				zk.exists(znode, true, this, null)
				return
		}

		byte[] b = null
		if (exists) {
			b = zk.getData(znode, false, null)
		}

		if (b == null && b != prevData
		|| (b != null && !Arrays.equals(prevData, b))) {
			listener.exsists(b)
			prevData = b
		}
	}

	@Override
	public void process(WatchedEvent event) {
		println "DataMonitor process()"
		String path = event.getPath()
		if (event.getType() == Event.EventType.None) {
			switch (event.getState()) {
				case KeeperState.SyncConnected:
					break
				case KeeperState.Expired:
					dead = true
					listener.closing(Code.SESSIONEXPIRED)
					break
			}
		} else {
			if (path != null && path.equals(znode)) {
				zk.exists(znode, true, this, null)
			}
		}
		if (chainWatcher != null) {
			chainWatcher.process(event)
		}
	}
	
	public boolean getDead() {
		return this.dead
	}
}