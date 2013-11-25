package org.apache.commons.io.monitor;

import java.io.File;

public class FileMonitorListener implements FileAlterationListener {
	private FileAlterationMonitor monitor;

	public FileMonitorListener(FileAlterationMonitor monitor) {
		this.monitor = monitor;
	}

	@Override
	public void onStart(FileAlterationObserver observer) {
		System.out.println("start");
	}

	@Override
	public void onDirectoryCreate(File directory) {
		System.out.println("directory create:" + directory);
	}

	@Override
	public void onDirectoryChange(File directory) {
		System.out.println("directory change:" + directory);
	}

	@Override
	public void onDirectoryDelete(File directory) {
		System.out.println("directory delete:" + directory);
	}

	@Override
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

	@Override
	public void onFileChange(File file) {
		System.out.println("file change:" + file);
	}

	@Override
	public void onFileDelete(File file) {
		System.out.println("file delete:" + file);
	}

	@Override
	public void onStop(FileAlterationObserver observer) {
		System.out.println("stop");
	}
}
