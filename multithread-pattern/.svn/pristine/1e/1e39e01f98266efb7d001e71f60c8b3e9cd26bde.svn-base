package com.thread.ch11_exam11_6;

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
		}
		return tsLog;
	}
}
