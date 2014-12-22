package com.thread.ch7_exam7_6;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

public class MiniServer {
	private final int portNumber;
	public MiniServer(int i) {
		this.portNumber = i;
		
	}
	public void execute() throws IOException {
		ServerSocket serverSocket = new ServerSocket(portNumber);
		System.out.println("Listening on " + serverSocket);
		try {
			while (true) {
				System.out.println("Accepting");
				final Socket clientSocket = serverSocket.accept();
				System.out.println("connected to " + clientSocket);
//				new Thread() {
//					public void run() {
						Service.service(clientSocket);
//					}
//				}.start();
			}
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			serverSocket.close();
		}
	}
	
}
