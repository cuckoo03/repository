package com.zookeeper.configuration;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.CountDownLatch;

import org.apache.thrift.TException;
import org.apache.thrift.protocol.TBinaryProtocol;
import org.apache.thrift.server.THsHaServer;
import org.apache.thrift.server.TServer;
import org.apache.thrift.transport.TFramedTransport;
import org.apache.thrift.transport.TNonblockingServerSocket;
import org.apache.thrift.transport.TTransportException;
import org.apache.zookeeper.CreateMode;
import org.apache.zookeeper.KeeperException;
import org.apache.zookeeper.WatchedEvent;
import org.apache.zookeeper.Watcher;
import org.apache.zookeeper.ZooDefs.Ids;
import org.apache.zookeeper.ZooKeeper;
import org.apache.zookeeper.data.Stat;

import com.thrift.HelloService;

/**
 * 주키퍼로 애플리케이션 환경 설정 관리 예제
 * 
 * @author cuckoo03
 *
 */
public class HelloServer implements Watcher {
	private static final String ZK_SERVERS = "192.168.1.101:2181";
	private static final int SESSION_TIMEOUT = 10 * 1000;
	private static final String HELLO_SERVICE_PATH = "/hello_service";
	private static final String CONF_PATH = HELLO_SERVICE_PATH + "/conf";
	public static final String CONF_GREETING_KEY = "hello.greeting";

	public static Map<String, String> configurations = new HashMap<>();
	private ZooKeeper zk;
	private CountDownLatch connMonitor = new CountDownLatch(1);

	public HelloServer() throws IOException, InterruptedException,
			KeeperException {
		zk = new ZooKeeper(ZK_SERVERS, SESSION_TIMEOUT, this);
		connMonitor.await();
		if (zk.exists(CONF_PATH, false) == null) {
			zk.create(HELLO_SERVICE_PATH, null, Ids.OPEN_ACL_UNSAFE,
					CreateMode.PERSISTENT);
			zk.create(CONF_PATH, null, Ids.OPEN_ACL_UNSAFE, CreateMode.PERSISTENT);
		}

		List<String> confNodes = zk.getChildren(CONF_PATH, this);
		for (String eachNode : confNodes) {
			byte[] confData = zk.getData(CONF_PATH + "/" + eachNode, this,
					new Stat());
			if (confData != null) {
				configurations.put(eachNode, new String(confData));
			}
		}
	}

	@Override
	public void process(WatchedEvent event) {
		if (event.getType() == Event.EventType.None) {
			if (event.getState() == Event.KeeperState.SyncConnected) {
				connMonitor.countDown();
			}
		} else if (event.getType() == Event.EventType.NodeChildrenChanged) {
			if (event.getPath().equals(CONF_PATH)) {
				reloadConfigurations();
			}
		} else if (event.getType() == Event.EventType.NodeDataChanged) {
			if (event.getPath().startsWith(CONF_PATH)) {
				String path = event.getPath();
				if (path.lastIndexOf("/") >= 0) {
					path = path.substring(path.lastIndexOf("/") + 1);
				}

				try {
					byte[] confData = zk.getData(event.getPath(), this,
							new Stat());
					synchronized (configurations) {
						if (confData != null) {
							configurations.put(path, new String(confData));
						}
					}
				} catch (KeeperException | InterruptedException e) {
					e.printStackTrace();
				}
			}
		}
	}

	public void startServer(int port) throws TTransportException {
		final TNonblockingServerSocket serverSocket = new TNonblockingServerSocket(
				port);
		final HelloService.Processor processor = new HelloService.Processor(
				new HelloHandler());
		final TServer server = new THsHaServer(processor, serverSocket,
				new TFramedTransport.Factory(), new TBinaryProtocol.Factory());

		System.err.println("HelloServer started(port:" + port + ")");
		server.serve();
	}

	private void reloadConfigurations() {
		try {
			List<String> children = zk.getChildren(CONF_PATH, this);
			List<String> addedNodes = new ArrayList<>();
			List<String> removeNodes = new ArrayList<>();

			findChangedChildren(configurations.keySet(), children, addedNodes,
					removeNodes);

			synchronized (configurations) {
				for (String eachNode : removeNodes) {
					configurations.remove(eachNode);
				}

				for (String eachNode : addedNodes) {
					byte[] confData = zk.getData(CONF_PATH + "/" + eachNode,
							false, new Stat());
					if (confData != null) {
						configurations.put(eachNode, new String(confData));
					}
				}
			}
		} catch (KeeperException | InterruptedException e) {
			e.printStackTrace();
		}
	}

	/**
	 * 
	 * @param cachedDatas
	 *            클라이언트에 저장된 데이터
	 * @param currentChildren
	 *            현재 서버에 저장된 데이터
	 * @param addedNodes
	 * @param removeNodes
	 */
	private void findChangedChildren(Set<String> cachedDatas,
			List<String> currentChildren, List<String> addedNodes,
			List<String> removeNodes) {
		Set<String> cachedSet = new HashSet<String>();
		cachedSet.addAll(cachedDatas);

		for (String eachNode : currentChildren) {
			if (cachedSet.remove(eachNode) == false) {
				removeNodes.add(eachNode);
			}
		}
		addedNodes.addAll(cachedSet);
	}

	public static void main(String[] args) throws Exception {
		new HelloServer().startServer(9001);
	}
}

/**
 * IDL에서 정의한 인터페이스를 구현한 클래스
 */
class HelloHandler implements HelloService.Iface {

	@Override
	public String greeting(String name, int age) throws TException {
		String greetingPrefix = "Hello";
		synchronized (HelloServer.configurations) {
			if (HelloServer.configurations
					.containsKey(HelloServer.CONF_GREETING_KEY)) {
				greetingPrefix = HelloServer.configurations
						.get(HelloServer.CONF_GREETING_KEY);
			}
		}
		return greetingPrefix + " " + name + ". You are " + age + " years old";
	}
}
