package com.thread.ch11_exam11_3;

public class Log {
	private static final ThreadLocal<TSLog> tslogCollection = new ThreadLocal<TSLog>();

	public static void println(String s) {
		getTSLog().println(s);
	}

	public static void close() {
		getTSLog().close();
	}

	private static TSLog getTSLog() {
		TSLog tsLog = tslogCollection.get();

		if (null == tsLog) {
			tsLog = new TSLog(Thread.currentThread().getName() + "-log.txt");
			tslogCollection.set(tsLog);
			startWatcher(tsLog);
		}
		return tsLog;
	}

	private static void startWatcher(final TSLog tsLog) {
		final Thread target = Thread.currentThread();
		final Thread watcher = new Thread() {
			public void run() {
				System.out.println("startWatcher for " + target.getName()
						+ " begin");

				try {
					target.join();
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
				tsLog.close();
				System.out.println("startWatcher for " + target.getName()
						+ " end");

			}
		};
		
		watcher.start();
	}
}
