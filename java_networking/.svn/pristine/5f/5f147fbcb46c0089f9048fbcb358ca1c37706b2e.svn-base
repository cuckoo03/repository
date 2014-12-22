package com.thread.ch9_exam9_3_1;

import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

public class Main {
	public static void main(String[] args) {
		long start = System.currentTimeMillis();

		Content content1 = Retriever.retrieve("http://www.yahoo.com");
		Content content2 = Retriever.retrieve("https://www.google.co.kr");

		saveToFile("yahoo.html", content1);
		saveToFile("google.html", content2);

		long end = System.currentTimeMillis();
		System.out.println("Elapsed time:" + (end - start) + "msec");
	}

	private static void saveToFile(String filename, Content content) {
		byte[] bytes = content.getBytes();
		System.out.println(Thread.currentThread().getName() + ": saving to "
				+ filename);
		FileOutputStream out;
		try {
			out = new FileOutputStream(filename);
			for (int i = 0; i < bytes.length; i++) {
				out.write(bytes[i]);
			}
			out.close();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}
