import org.apache.commons.daemon.Daemon;
import org.apache.commons.daemon.DaemonContext;
import org.apache.commons.daemon.DaemonInitException;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

public class DaemonTest implements Daemon {
	private Log log = LogFactory.getLog(getClass());
	private Thread t = null;

	public DaemonTest() {
		System.out.println("demonTest");
	}
	public void destroy() {
		log.info("destory");
	}

	public void init(DaemonContext arg0) throws DaemonInitException, Exception {
		log.info("init");
		t = new DaemonThread();
	}

	public void start() throws Exception {
		log.info("start");
		System.out.println("start");
		t.start();
	}

	public void stop() throws Exception {
		log.info("stop");
		t.interrupt();
	}
}

class DaemonThread extends Thread {
	private Log log = LogFactory.getLog(getClass());

	@Override
	public void run() {
		while (Thread.interrupted()) {
			log.info("run");
			try {
				Thread.sleep(5000);
			} catch (InterruptedException e) {
				Thread.currentThread().interrupt();
				e.printStackTrace();
			}
		}
	}
}