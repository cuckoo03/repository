package ch08;

import org.apache.curator.RetryPolicy;
import org.apache.curator.framework.CuratorFramework;
import org.apache.curator.framework.CuratorFrameworkFactory;
import org.apache.curator.retry.ExponentialBackoffRetry;

public class SimpleCurator {
	public void testCuratorConnection() throws Exception {
		String connectionString = "192.168.1.101:2181";
		RetryPolicy retryPolicy = new ExponentialBackoffRetry(1000, 3);

		CuratorFramework client = CuratorFrameworkFactory.builder()
				.connectString(connectionString).retryPolicy(retryPolicy)
				.sessionTimeoutMs(10 * 6000).build();

		client.start();
		client.getZookeeperClient().blockUntilConnectedOrTimedOut();
		client.create().forPath("/new-node");
		client.close();

	}

	public static void main(String[] args) {
		SimpleCurator curator = new SimpleCurator();
		try {
			curator.testCuratorConnection();
		} catch (InterruptedException e) {
			e.printStackTrace();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
