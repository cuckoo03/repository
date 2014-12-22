package com;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.net.UnknownHostException;

public class Exam8_8_client {

	/**
	 * @param args
	 * @throws IOException 
	 */
	public static void main(String[] args) throws IOException {
		Socket socket = null;
		BufferedReader br = null;
		PrintWriter pw = null;
		boolean endflag = false;
		try {
			socket = new Socket("127.0.0.1", 10001);
			br = new BufferedReader(new InputStreamReader(socket
					.getInputStream()));
			pw = new PrintWriter(socket.getOutputStream());
			BufferedReader keyboard = new BufferedReader(new InputStreamReader(
					System.in));

			System.out.print("방이름을 입력하세요>");
			pw.println(keyboard.readLine());
			pw.flush();
			
			System.out.print("id를 입력하세요>");
			pw.println(keyboard.readLine());
			pw.flush();
			
			
			InputThread2 it = new InputThread2(socket, br);
			it.start();
			String line = null;
			while ((line = keyboard.readLine()) != null) {
				pw.println(line);
				pw.flush();
				if (line.equals("/quit")) {
					endflag = true;
					break;
				}
			}
			System.out.println("클라이언트의 접속을 종료합니다.");
		} catch (UnknownHostException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
			br.close();
			pw.close();
			socket.close();
		}
	}
}

class InputThread2 extends Thread {
	private Socket socket = null;
	private BufferedReader br = null;
	public InputThread2(Socket socket, BufferedReader br) {
		this.socket = socket;
		this.br = br;
	}
	public void run() {
		try {
			String line = null;
			while ((line = br.readLine()) != null) {
				System.out.println(line);
			}
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			try {
				br.close();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			try {
				socket.close();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}
}