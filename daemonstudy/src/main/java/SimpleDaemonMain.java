import java.io.IOException;

import net.kldp.jsd.IllegalSimpleDaemonClassException;
import net.kldp.jsd.LockFileExistException;
import net.kldp.jsd.SimpleDaemon;
import net.kldp.jsd.SimpleDaemonManager;

public class SimpleDaemonMain implements SimpleDaemon {
	public static void main(String[] args) {
		if (args.length > 0 && args[0].equals("shutdown")) {
			System.out.println("shutdown start");
			SimpleDaemonManager sdm;
			try {
				sdm = SimpleDaemonManager.getInstance(SimpleDaemonMain.class);
				sdm.shutdownDaemon();
			} catch (IllegalSimpleDaemonClassException e) {
				e.printStackTrace();
			} catch (IOException e1) {
				e1.printStackTrace();
			}
			return;
		}

		SimpleDaemonManager sdm = null;
		try {
			sdm = SimpleDaemonManager.getInstance(SimpleDaemonMain.class);
			sdm.start();
		} catch (IllegalSimpleDaemonClassException e) {
			e.printStackTrace();
		} catch (LockFileExistException e) {
			System.out.println("daemon still running");
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	@Override
	public void shutdown() {
		System.out.println("shutdown daemon");
	}

	@Override
	public void startDaemon() {
		System.out.println("start daemon");
		while (true) {
			try {
				System.out.print(". ");
				Thread.sleep(1000);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
	}
}
