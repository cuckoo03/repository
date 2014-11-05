package com.zookeeper.cluster;

import java.io.IOException;
import java.net.InetAddress;
import java.util.concurrent.CountDownLatch;

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
import org.apache.zookeeper.ZooKeeper;
import org.apache.zookeeper.ZooDefs.Ids;

import com.thrift.HelloHandler;
import com.thrift.HelloService;

public class HelloServer implements Watcher {
	private static final int ZK_SESSION_TIMEOUT = 10 * 1000;
	private static final String ZK_SERVERS = "192.168.1.101:2181";
	private static final String HELLO_GROUP = "/helloserver";

	private ZooKeeper zk;
	private CountDownLatch connMonitor = new CountDownLatch(1);

	@Override
	public void process(WatchedEvent event) {
		System.out.println("receice zk event:" + event);
		if (event.getType() == Event.EventType.None) {
			if (event.getState() == Event.KeeperState.SyncConnected) {
				connMonitor.countDown();
			}
		}
	}

	public void runServer(int port) throws TTransportException, IOException,
			InterruptedException, KeeperException {
		final TNonblockingServerSocket socket = new TNonblockingServerSocket(
				port);
		final HelloService.Processor processor = new HelloService.Processor(
				new HelloHandler());
		final TServer server = new THsHaServer(processor, socket,
				new TFramedTransport.Factory(), new TBinaryProtocol.Factory());

		serverStarted(port);
		System.err.println("Helloserver started(port:" + port + ")");
		server.serve();
	}

	private void serverStarted(int port) throws IOException,
			InterruptedException, KeeperException {
		zk = new ZooKeeper(ZK_SERVERS, ZK_SESSION_TIMEOUT, this);
		connMonitor.await();
		if (zk.exists(HELLO_GROUP, false) == null) {
			zk.create(HELLO_GROUP, null, Ids.OPEN_ACL_UNSAFE,
					CreateMode.PERSISTENT);
		}

		String serverInfo = InetAddress.getLocalHost().getHostAddress() + ":"
				+ port;
		zk.create(HELLO_GROUP + "/" + serverInfo, null, Ids.OPEN_ACL_UNSAFE,
				CreateMode.EPHEMERAL);
	}

	public static void main(String[] args) throws TTransportException,
			IOException, InterruptedException, KeeperException {
		new HelloServer().runServer(9001);
	}
}
