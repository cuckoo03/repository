package com.thread.ch11_list11_1;

import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;

public class Log {
	private static PrintWriter writer;
	
	static {
		try {
			writer = new PrintWriter(new FileWriter("log.txt"));
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	public static void println(String s) {
		writer.println(s);
	}
	
	public static void close() {
		writer.println("---end of log---");
		writer.close();
	}
}
