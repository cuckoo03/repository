package client2

import org.apache.zookeeper.CreateMode
import org.apache.zookeeper.KeeperException
import org.apache.zookeeper.WatchedEvent
import org.apache.zookeeper.Watcher
import org.apache.zookeeper.ZooKeeper
import org.apache.zookeeper.AsyncCallback.ChildrenCallback
import org.apache.zookeeper.AsyncCallback.StatCallback
import org.apache.zookeeper.AsyncCallback.StringCallback
import org.apache.zookeeper.AsyncCallback.VoidCallback
import org.apache.zookeeper.Watcher.Event
import org.apache.zookeeper.Watcher.Event.KeeperState
import org.apache.zookeeper.ZooDefs.Ids
import org.apache.zookeeper.data.Stat

class ZooKeeperClient implements Watcher, Runnable, ChildrenCallback, 
	StatCallback, StringCallback, VoidCallback{
	private ZooKeeper zk
	private boolean dead
	private String znode

	public ZooKeeperClient(String hostPort, String znode, String localIp) {
		this.znode = znode
		zk = new ZooKeeper(hostPort, 6000, this)
		if (zk.exists(znode, false) == null) {
			zk.create(znode, localIp.getBytes(), Ids.OPEN_ACL_UNSAFE,
					CreateMode.EPHEMERAL, this)
			println "Create node data: " + 
				new String(zk.getData(znode, false, null))
		} else {
			println "Node exists:" + new String(zk.getData(znode, false, null))
			zk.exists(znode, true, this, null)
		}
		
		sleep(5000)
		zk.getChildren(znode, true, this, null)
		
		/*
		sleep(5000)
		println "before Creation..."
		zk.create(znode + "/c4", localIp.getBytes(), Ids.OPEN_ACL_UNSAFE,
				CreateMode.EPHEMERAL, this, null)
		println "Create node data: " + 
				new String(zk.getData(znode + "/c4", false, null))
		
		sleep(5000)
		println "before Creation..."
		zk.create(znode + "/c5", localIp.getBytes(), Ids.OPEN_ACL_UNSAFE,
				CreateMode.EPHEMERAL, this, null)
		println "Create node data: " +
				new String(zk.getData(znode + "/c5", false, null))
				*/
				
		/*
		sleep(5000)
		println "before Deletion..."
		println "Delete node data: " +
		new String(zk.getData(znode + "/c4", false, null))
		zk.delete(znode + "/c4", 0, this, null)
		*/
	}
	
	/**
	 * 서버로부터 새로운 이벤트 발생시 실행된다
	 * ## process path: null, eventType:None, eventState:SyncConnected
	 * ## process path: /zk_test, eventType:NodeDeleted, eventState:SyncConnected
	 */
	@Override
	public void process(WatchedEvent event) {
		println "process()"
		String path = event.getPath()
		String eventType = event.getType().name()
		String eventState = event.getState().name()
		println "## process path: $path, eventType:$eventType, eventState:$eventState"

		if (event.getType() == Event.EventType.NodeChildrenChanged) {
			zk.getChildren(znode, true, this, null)
		}
	}
	@Override
	public void run() {
		println "run()"
		try {
			synchronized (this) {
				while (!dead) {
					wait()
				}
			}
		} catch (InterruptedException e) {
		}
	}
	/**
	 * getChildren
	 * ChildrenCallback
	 * zk.getChildren() 함수의 콜백함수
	 * processResult:rc=0, path=/servers, child=c4, data=192.168.1.101
	 */
	@Override
	public void processResult(int rc, String path, Object ctx,
			List<String> children) {
			println "ChildrenCallback.processResult()"
		children.each { child ->
			byte[] data = null
			try {
				data = zk.getData(znode + "/" + child, false, null)
			} catch (KeeperException e) {
				e.printStackTrace()
			} catch (InterruptedException e) {
				return
			}
			println "## processResult:rc=$rc, path=$path, child=$child, data=" + new String(data)
		}
		
		println ""
	}
	
	/**
	 * exists()
	 * StatCallback
	 * rc:0, ctx:null, path:/servers, stat:17179869251,17179869251,1410417367192,1410417367192,0,52,0,0,9,0,17179869454
	 */
	@Override
	public void processResult(int rc, String path, Object ctx, Stat stat) {
		println "StatCallback.processResult()"
		println "rc:$rc, ctx:$ctx, path:$path, stat:$stat"
		println ""
	}
	
	/**
	 * create()
	 * StringCallback
	 * rc:0, path:/servers/c4, ctx:null, name:/servers/c4
	 */
	@Override
	public void processResult(int rc, String path, Object ctx, String name) {
		println "StringCallback.processResult()"
		println "rc:$rc, path:$path, ctx:$ctx, name:$name"
		println ""
	}
	
	/**
	 * delete
	 * VoidCallback
	 * rc:0, path:/servers/c4, ctx:null
	 */
	@Override
	public void processResult(int rc, String path, Object ctx) {
		println "VoidCallback.processResult()"
		println "rc:$rc, path:$path, ctx:$ctx"
		println ""
	}
	
	static main(args) {
		String hostPort = "192.168.1.101:2181"
		String znode = "/servers"
		String localIp = "192.168.1.101"

		try {
			new ZooKeeperClient(hostPort, znode, localIp).run()
		} catch (Exception e) {
			e.printStackTrace()
		}
		println "end"
	}
}