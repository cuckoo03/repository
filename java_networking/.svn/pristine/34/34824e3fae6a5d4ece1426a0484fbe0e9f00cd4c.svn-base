package com;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.net.Socket;

public class Exam8_2 {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		// TODO Auto-generated method stub
		try {
			Socket socket = new Socket("127.0.0.1", 10001);
			BufferedReader keyboard = new BufferedReader(new InputStreamReader(System.in));
			OutputStream os = socket.getOutputStream();
			InputStream is = socket.getInputStream();
			//BufferedWriter pw = new BufferedWriter(new PrintWriter(os));
			PrintWriter pw = new PrintWriter(os);
			BufferedReader br = new BufferedReader(new InputStreamReader(is));
			String line = null;
			while ((line = keyboard.readLine()) != null) {
				if (line.equals("quit")) {
					System.out.println("종료");
					break;
				}
				pw.println(line);
				//pw.write(line);
				pw.flush();
				String echo = br.readLine();
				System.out.println("서버로부터 받은 문자열:" + echo);
			}
			pw.close();
			br.close();
			socket.close();
		}catch (Exception e) {
			e.printStackTrace();
		}
	}

}
