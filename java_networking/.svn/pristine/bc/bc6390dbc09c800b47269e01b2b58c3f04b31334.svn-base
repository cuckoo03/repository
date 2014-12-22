package com;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.Socket;

import javax.xml.transform.stream.StreamResult;

public class Exam8_1 {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		// TODO Auto-generated method stub
		try {
			ServerSocket server = new ServerSocket(10001);
			System.out.println("접속을 기다립니다");
			
			Socket socket = server.accept();
			InetAddress inetaddr = socket.getInetAddress();
			System.out.println(inetaddr.getHostAddress() + "로부터 접속");
			
			OutputStream out = socket.getOutputStream();
			InputStream in = socket.getInputStream();
			PrintWriter pw = new PrintWriter(new OutputStreamWriter(out));
			BufferedReader br = new BufferedReader(new InputStreamReader(in));
			
			String line = "";
			while ((line = br.readLine()) != null) {
				System.out.println("클라이언트로부터 받은 문자열:" + line);
				pw.println(line);
				pw.flush();
			}
			pw.close();
			br.close();
			socket.close();
			
		} catch (Exception e) {
			
		}
	}

}
