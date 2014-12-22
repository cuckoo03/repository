package com.thread.ch04_list4_1;

import java.io.FileWriter;
import java.io.IOException;
import java.io.Writer;

public class Data {
	private final String filename;
	private String content;
	volatile private boolean changed;

	public Data(String filename, String content) {
		this.filename = filename;
		this.content = content;
		this.changed = true;
	}

	public synchronized void change(String newContent) {
		content = newContent;
		changed = true;
	}

	public synchronized void save() throws IOException, InterruptedException {
		System.out.println(Thread.currentThread().getName() + " : save begin");
		if (!changed) {
			System.out.println(Thread.currentThread().getName()
					+ " : balk");
			return;
		}
		System.out.println(Thread.currentThread().getName() + " : save end");
		Thread.sleep(1000);
		doSave();
		changed = false;
	}

	private void doSave() throws IOException {
		System.out.println(Thread.currentThread().getName()
				+ " calls doSave, content=" + content);
		Writer writer = new FileWriter(filename);
		writer.write(content);
		writer.close();
	}
}
