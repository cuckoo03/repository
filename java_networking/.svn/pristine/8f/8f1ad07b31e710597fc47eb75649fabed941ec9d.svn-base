package com;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;

import javax.net.ssl.HttpsURLConnection;

public class Exam8_5 {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		// TODO Auto-generated method stub
		try {
			ServerSocket server = new ServerSocket(80);
			while (true) {
				System.out.println("wait");
				Socket socket = server.accept();
				System.out.println("start thread");
				HttpThread ht = new HttpThread(socket);
				ht.start();
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

}
class HttpThread extends Thread {
	private Socket socket = null;
	private BufferedReader br = null;
	private PrintWriter pw = null;
	public HttpThread(Socket socket) {
		this.socket = socket;
		
		try {
			br = new BufferedReader(new InputStreamReader(socket.getInputStream()));
			pw = new PrintWriter(socket.getOutputStream());
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	public void run() {
		BufferedReader fbr = null;
		try {
			String line = br.readLine();
			int start = line.indexOf(" ") + 2;
			int end = line.lastIndexOf("HTTP") - 1;
			String filename = line.substring(start, end);
			if (filename.equals("")) {
				filename = "index.html";
			}
			System.out.println("사용자가" + filename + "을 요청했습니다.");
			fbr = new BufferedReader(new FileReader(filename));
			String fline = null;
			while ((fline = fbr.readLine()) != null) {
				pw.println(fline);
				pw.flush();
			}
			br.close();
			pw.close();
			
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
