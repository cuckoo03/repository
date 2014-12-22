package com.corejava.chap003;

import java.net.Socket;

public class SocketOpenerTest {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		// TODO Auto-generated method stub
		String host = "127.0.0.1";
		int port = 10001;
		int timeout = 50000;
		Socket s = SocketOpener.openSocket(host, port, timeout);
		if (null == s){
			System.out.println("socket could not be opened");
		} else {
			System.out.println(s);
		}
	}

}
