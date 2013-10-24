package org.apache.commons.io.monitor;

import java.io.File;
import java.lang.management.ManagementFactory;

import org.apache.commons.io.filefilter.FileFilterUtils;
import org.apache.commons.io.filefilter.HiddenFileFilter;
import org.apache.commons.io.filefilter.IOFileFilter;

public class FileMonitorMain {
	private static final String FILE_TO_MONITOR = "c:/logs";
	private static final long INTERVAL = 1000L;

	public void monitoring() throws Exception {

		System.out.println("begin observer");

		File directory = new File(FILE_TO_MONITOR);

		IOFileFilter directories = FileFilterUtils
				.and(FileFilterUtils.directoryFileFilter(),
						HiddenFileFilter.VISIBLE);
		IOFileFilter files = FileFilterUtils.and(
				FileFilterUtils.fileFileFilter(),
				FileFilterUtils.suffixFileFilter(".java"));
		IOFileFilter filter = FileFilterUtils.or(directories, files);
		FileAlterationObserver observer = new FileAlterationObserver(directory,
				filter);
		FileAlterationMonitor monitor = new FileAlterationMonitor(INTERVAL);

		observer.addListener(new FileMonitorListener(monitor));
		monitor.addObserver(observer);

		monitor.start();
	}

	public static void main(String[] args) {
		System.out.println("Process info:"
				+ ManagementFactory.getRuntimeMXBean().getName());
		FileMonitorMain monitorMain = new FileMonitorMain();

		try {
			monitorMain.monitoring();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
