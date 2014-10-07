package exam.lock

import java.util.List;

import org.apache.zookeeper.AsyncCallback.ChildrenCallback;
import org.apache.zookeeper.CreateMode
import org.apache.zookeeper.WatchedEvent
import org.apache.zookeeper.Watcher
import org.apache.zookeeper.ZooKeeper
import org.apache.zookeeper.ZooDefs.Ids

/**
 * 그루비 버전 락종료 exam
 * @author cuckoo03
 *
 */
class GLockTest implements Runnable, Watcher, ChildrenCallback {
	private ZooKeeper zk
	private static String znodeChild
	private volatile boolean dead

	public GLockTest(String znodeChild) {
		this.znodeChild = znodeChild
		zk = new ZooKeeper("192.168.1.101:2181", 6000, this)
	}
	@Override
	public void run() {
		println "run()"
		try {
			synchronized (this) {
				while (!dead) {
					wait()
				}
				println "dead"
			}
		} catch (InterruptedException e) {
		}
	}
	@Override
	public void process(WatchedEvent event) {
		println "process():" + event.getType() + "," + event.getState()
	}
	/**
	 * ChildrenCallback
	 */
	@Override
	public void processResult(int rc, String path, Object ctx,
			List<String> children) {
		byte[] data = null
		children.each { child ->
			println "rc:$rc, path:$path, ctx:$ctx, child:$child"
			data = zk.getData("/servers/" + child, false, null)

			if (znodeChild == child) {
				println "current data:" + new String(data)
				println "busy...5 seconds"
				sleep(10000)
				println "work complete..."

				println "node delete:" + new String(data)
				zk.delete("/servers/" + child, 0)
				println "delete complete"
				synchronized (this) {
					dead = true
					this.notifyAll()
				}
			}
		}
	}
			
	private void execute() {
		if (zk.exists("/servers" + "/" + znodeChild, false) == null) {
			zk.create("/servers" + "/" + znodeChild, znodeChild.getBytes(),
					Ids.OPEN_ACL_UNSAFE, CreateMode.EPHEMERAL)
			println "node create"
		} else {
			println "node exist"
		}

		zk.getChildren("/servers", true, this, null)
	}
	static main(args) {
		if (args.length < 1) {
			println "not enough args"
			return
		}
		LockTest test = new LockTest(args[0])
		test.execute()
		new Thread(test).start()
	}
}