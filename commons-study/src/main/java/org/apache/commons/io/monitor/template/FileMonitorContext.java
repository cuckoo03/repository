package org.apache.commons.io.monitor.template;

public class FileMonitorContext {
	private String fileToMonitor;
	private long interval;

	public FileMonitorContext(String fileToMonitor, long interval) {
		this.fileToMonitor = fileToMonitor;
		this.interval = interval;
	}

	public String getFileToMonitor() {
		return fileToMonitor;
	}

	public void setFileToMonitor(String fileToMonitor) {
		this.fileToMonitor = fileToMonitor;
	}

	public long getInterval() {
		return interval;
	}

	public void setInterval(long interval) {
		this.interval = interval;
	}
}
