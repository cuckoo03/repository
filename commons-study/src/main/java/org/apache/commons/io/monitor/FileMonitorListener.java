package org.apache.commons.io.monitor;

import java.io.File;

public class FileMonitorListener implements FileAlterationListener {
	private FileAlterationMonitor monitor;

	public FileMonitorListener(FileAlterationMonitor monitor) {
		this.monitor = monitor;
	}

	public void onStart(FileAlterationObserver observer) {
		System.out.println("start");
	}

	public void onDirectoryCreate(File directory) {
		System.out.println("directory create:" + directory);
	}

	public void onDirectoryChange(File directory) {
		System.out.println("directory change:" + directory);
	}

	public void onDirectoryDelete(File directory) {
		System.out.println("directory delete:" + directory);
	}

	public void onFileCreate(File file) {
		System.out.println("file create:" + file);
		if ("end".equals(file.getName().toLowerCase())) {
			try {
				monitor.stop();
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}

	public void onFileChange(File file) {
		System.out.println("file change:" + file);
	}

	public void onFileDelete(File file) {
		System.out.println("file delete:" + file);
	}

	public void onStop(FileAlterationObserver observer) {
		System.out.println("stop");
	}
}
