package com.thread.ch7_exam7_6_1;

import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;

public class Service {
	public static void service(Socket clientSocket) {
		System.out.println(Thread.currentThread().getName()
				+ ":Service.service(" + clientSocket + ") begin");

		try {
			DataOutputStream out = new DataOutputStream(
					clientSocket.getOutputStream());
			out.writeBytes("HTTP/1.0 200 OK\r\n");
			out.writeBytes("Content-Type: text/html\r\n");
			out.writeBytes("\r\n");
			out.writeBytes("<html><head><title>countdown</title></head><body>");
			for (int i = 0; i < 10; i++) {
				out.writeBytes("" + i + "");
				System.out.println(i);
				out.flush();
				try {
					Thread.sleep(1000);
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
			}
			out.writeBytes("</body></html>");
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			try {
				clientSocket.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		System.out.println(Thread.currentThread().getName()
				+ ":Service.service(" + clientSocket + ") end");
	}
}
