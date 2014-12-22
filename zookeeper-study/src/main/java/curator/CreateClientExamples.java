package curator;

import java.util.ArrayList;
import java.util.List;

import org.apache.curator.RetryPolicy;
import org.apache.curator.framework.CuratorFramework;
import org.apache.curator.framework.CuratorFrameworkFactory;
import org.apache.curator.framework.api.ACLProvider;
import org.apache.curator.retry.ExponentialBackoffRetry;
import org.apache.zookeeper.ZooDefs;
import org.apache.zookeeper.data.ACL;
import org.junit.Test;

public class CreateClientExamples {
	static class UserACLProvider implements ACLProvider {
		List<ACL> acl = new ArrayList<ACL>();

		UserACLProvider() {
			acl.addAll(ZooDefs.Ids.CREATOR_ALL_ACL);
			acl.addAll(ZooDefs.Ids.READ_ACL_UNSAFE);
		}

		@Override
		public List<ACL> getDefaultAcl() {
			return acl;
		}

		@Override
		public List<ACL> getAclForPath(String path) {
			return acl;
		}
	}

	@Test
	public void testCuratorConn() throws Exception {
		String connectionString = "192.168.1.101:2181/servers";

		RetryPolicy retryPolicy = new ExponentialBackoffRetry(1000, 3);
		CuratorFramework client = CuratorFrameworkFactory.builder()
				.connectString(connectionString).retryPolicy(retryPolicy)
				.aclProvider(new UserACLProvider())
				.authorization("zo_user", "testdrive".getBytes())
				.sessionTimeoutMs(1000 * 10).build();
		client.start();
		
		client.getZookeeperClient().blockUntilConnectedOrTimedOut();
		
		client.create().forPath("/new");
		client.close();
	}
}
