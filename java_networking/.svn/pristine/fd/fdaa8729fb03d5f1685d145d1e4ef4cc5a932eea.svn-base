package com;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.Socket;

public class Exam8_3 {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		// TODO Auto-generated method stub
		try {
			ServerSocket server = new ServerSocket(10001);
			System.out.println("waiting");
			while (true) {
				Socket socket = server.accept();
				EchoThread echoThread = new EchoThread(socket);
				echoThread.start();
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}

class EchoThread extends Thread {
	private Socket socket;

	public EchoThread(Socket socket) {
		this.socket = socket;
	}

	public void run() {
		try {
			InetAddress inetaddr = socket.getInetAddress();
			System.out.println(inetaddr.getHostAddress() + "is connected");
			
			OutputStream os = socket.getOutputStream();
			InputStream is = socket.getInputStream();
			PrintWriter pw = new PrintWriter(os);
			BufferedReader br = new BufferedReader(new InputStreamReader(is));
			String line = null;
			while ((line = br.readLine()) != null) {
				System.out.println("from server:" + line);
				pw.println(line);
				pw.flush();
			}
			pw.close();
			br.close();
			socket.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
