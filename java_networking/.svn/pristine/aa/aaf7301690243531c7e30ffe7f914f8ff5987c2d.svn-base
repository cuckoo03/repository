package com.corejava.chap003;

import java.io.IOException;
import java.net.Socket;
import java.net.UnknownHostException;

public class SocketOpener implements Runnable {

	private Socket socket;
	private String host;
	private int port;

	public SocketOpener(String host, int port) {
		// TODO Auto-generated constructor stub
		this.socket = null;
		this.host = host;
		this.port = port;
	}

	public static Socket openSocket(String host, int port, int timeout) {
		// TODO Auto-generated method stub
		SocketOpener opener = new SocketOpener(host, port);
		Thread t = new Thread(opener);
		t.start();
		try {
			t.join(timeout);
		} catch (InterruptedException e) {

		}
		return opener.getSocket();
	}

	private Socket getSocket() {
		// TODO Auto-generated method stub
		return socket;
	}

	public void run() {
		// TODO Auto-generated method stub
		try {
			socket = new Socket(host, port);
		} catch (UnknownHostException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

}
