package com.zookeeper.cluster;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.concurrent.CountDownLatch;

import org.apache.thrift.TException;
import org.apache.thrift.protocol.TBinaryProtocol;
import org.apache.thrift.transport.TFramedTransport;
import org.apache.thrift.transport.TSocket;
import org.apache.thrift.transport.TTransport;
import org.apache.thrift.transport.TTransportException;
import org.apache.zookeeper.KeeperException;
import org.apache.zookeeper.WatchedEvent;
import org.apache.zookeeper.Watcher;
import org.apache.zookeeper.ZooKeeper;

import com.thrift.HelloService;

/**
 * 주키퍼를 이용한 클러스터 멤버십(그룹 멤버십)
 * 현재 실행 중인 서버 목록을 유지하고 새로 추가하거나 장애나 점검등으로 제거되는 서버를 제거하는 기능
 * 
 * @author cuckoo03
 *
 */
public class HelloClient implements Watcher {
	private static final String SERVER = "/servers";
	private CountDownLatch connMonitor = new CountDownLatch(1);
	private Object nodeMonitor = new Object();
	private List<String> servers = new ArrayList<>();
	private ZooKeeper zk;
	private Random random = new Random();

	@Override
	public void process(WatchedEvent event) {
		if (event.getType() == Event.EventType.None) {
			if (event.getState() == Event.KeeperState.SyncConnected) {
				connMonitor.countDown();
			}
		}
	}

	public void startClient() throws IOException, InterruptedException,
			KeeperException, TException {
		zk = new ZooKeeper("192.168.1.101:2181", 4 * 1000, this);
		connMonitor.await();

		synchronized (nodeMonitor) {
			loadHelloServers();
			if (servers.size() == 0) {
				nodeMonitor.wait();
			}
		}

		while (true) {
			doSomething();
		}
	}

	private void doSomething() throws TException {
		String helloServer = servers.get(0);
//		String helloServer = servers.get(random.nextInt(servers.size() - 1));

		// ip:port
		String[] serverInfos = helloServer.split(":");
		TSocket socket = new TSocket(serverInfos[0],
				Integer.parseInt(serverInfos[1]));
		socket.setTimeout(5 * 1000);
		TTransport transport = new TFramedTransport(socket);
		HelloService.Client client = new HelloService.Client(
				new TBinaryProtocol(transport));

		transport.open();

		String result = client.greeting("test", 1);
		System.out.println("received [" + result + "]");
		
		transport.close();
		
		try {
			Thread.sleep(5 * 1000);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		
		synchronized (nodeMonitor) {
			nodeMonitor.notifyAll();
			System.out.println("notifyAll");
			
		}
	}

	private void loadHelloServers() throws KeeperException,
			InterruptedException {
		servers.clear();
		servers.addAll(zk.getChildren(SERVER, new HelloServerWatcher()));
	}

	class HelloServerWatcher implements Watcher {
		@Override
		public void process(WatchedEvent event) {
			synchronized (nodeMonitor) {
				try {
					loadHelloServers();
				} catch (KeeperException e) {
					e.printStackTrace();
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
			}
		}
	}

	public static void main(String[] args) {
		try {
			new HelloClient().startClient();
		} catch (IOException | InterruptedException | KeeperException
				| TException e) {
			e.printStackTrace();
		}
	}
}
