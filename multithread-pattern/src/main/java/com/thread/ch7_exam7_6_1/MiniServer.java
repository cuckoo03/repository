package com.thread.ch7_exam7_6_1;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class MiniServer {
	private final int portNumber;

	public MiniServer(int i) {
		this.portNumber = i;

	}

	public void execute() throws IOException {
		ServerSocket serverSocket = new ServerSocket(portNumber);
		ExecutorService executorService = Executors.newCachedThreadPool();
		System.out.println("Listening on " + serverSocket);
		try {
			while (true) {
				System.out.println("Accepting");
				final Socket clientSocket = serverSocket.accept();
				System.out.println("connected to " + clientSocket);
				executorService.execute(new Thread() {
					public void run() {
						Service.service(clientSocket);
					}
				});
			}
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			executorService.shutdown();
			serverSocket.close();
		}
	}

}
