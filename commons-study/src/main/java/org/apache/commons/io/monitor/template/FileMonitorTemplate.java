package org.apache.commons.io.monitor.template;

import java.io.File;

import org.apache.commons.io.filefilter.IOFileFilter;
import org.apache.commons.io.monitor.FileAlterationListener;
import org.apache.commons.io.monitor.FileAlterationMonitor;
import org.apache.commons.io.monitor.FileAlterationObserver;

public class FileMonitorTemplate implements Bootstrap {
	private FileMonitorContext context;
	private IOFileFilter filter;
	private FileAlterationListener listener;
	private FileAlterationMonitor monitor = null;

	public FileMonitorTemplate(FileMonitorContext context, IOFileFilter filter,
			FileAlterationListener listener) {
		this.context = context;
		this.filter = filter;
		this.listener = listener;
	}

	public void start() throws Exception {
		File directory = new File(context.getFileToMonitor());

		FileAlterationObserver observer = new FileAlterationObserver(directory,
				filter);
		monitor = new FileAlterationMonitor(context.getInterval());

		observer.addListener(listener);
		monitor.addObserver(observer);

		monitor.start();
	}

	public void stop() throws Exception {
		monitor.stop();
	}
}