package exam.lock;

import java.io.IOException;
import java.util.List;

import org.apache.zookeeper.CreateMode;
import org.apache.zookeeper.KeeperException;
import org.apache.zookeeper.WatchedEvent;
import org.apache.zookeeper.Watcher;
import org.apache.zookeeper.AsyncCallback.ChildrenCallback;
import org.apache.zookeeper.ZooDefs.Ids;
import org.apache.zookeeper.ZooKeeper;


/**
 * 분산락 구현
 * 노드 삭제 이벤트 호출뒤에 자신의 노드차례가 오면 10초 작업 뒤에 자신의 노드를 삭제하고 종료한다.
 * @author cuckoo03
 *
 */
public class LockTest implements Runnable, Watcher, ChildrenCallback {
	private final ZooKeeper zk;
	private final String znodeChild;
	private boolean dead;

	public LockTest(String znodeChild) throws IOException {
		this.znodeChild = znodeChild;
		zk = new ZooKeeper("192.168.1.101:2181", 6000, this);
	}

	@Override
	public void run() {
		System.out.println("run()");
		try {
			synchronized (this) {
				while (!dead) {
					this.wait();
				}
			}
			System.out.println("dead");
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}

	@Override
	public void process(WatchedEvent event) {
		System.out.println("process():" + event.getType() + ","
				+ event.getState());
		if (event.getType() == Event.EventType.NodeChildrenChanged) {
			zk.getChildren("/servers", true, this, null);
		}
	}

	@Override
	public void processResult(int rc, String path, Object ctx,
			List<String> children) {
		System.out.println("ChildrenCallback.processResult()");
		byte[] data = null;
		for (int i = 0; i < children.size(); i++) {
			String child = children.get(0);
			System.out.println("current:" + znodeChild + ", rc:" + rc
					+ ",path:" + path + ",ctx:" + ctx + ",child:" + child);
			try {
				data = zk.getData("/servers/" + child, false, null);
				if (znodeChild.equals(child)) {
					System.out.println("current data:" + new String(data));
					System.out.println("busy 10 seconds");
					Thread.sleep(10000);
					System.out.println("Work complete");

					System.out.println("node delete:" + new String(data));
					zk.delete("/servers/" + child, 0);
					System.out.println("Delete complete");
					synchronized (this) {
						dead = true;
						this.notifyAll();
					}
				}
			} catch (KeeperException | InterruptedException e) {
				e.printStackTrace();
			}
		}
	}

	private void execute() throws KeeperException, InterruptedException {
		if (zk.exists("/servers" + "/" + znodeChild, false) == null) {
			System.out.println("node create");
			zk.create("/servers" + "/" + znodeChild, znodeChild.getBytes(),
					Ids.OPEN_ACL_UNSAFE, CreateMode.EPHEMERAL);
		} else {
			System.out.println("node exsist");
		}

		Thread.sleep(5000);
		zk.getChildren("/servers", true, this, null);
	}

	public static void main(String[] args) {
		if (args.length < 1) {
			System.out.println("Not enough args.");
			System.out.println("> [znode]");
			return;
		}
		try {
			LockTest test = new LockTest(args[0]);
			test.execute();
			new Thread(test).start();
		} catch (IOException e) {
			e.printStackTrace();
		} catch (KeeperException e) {
			e.printStackTrace();
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}
}
