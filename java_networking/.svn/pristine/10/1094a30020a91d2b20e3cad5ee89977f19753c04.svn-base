package com;

import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.ServerSocket;
import java.net.Socket;

public class Exam8_11 {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		Socket socket = null;
		ObjectOutputStream oos = null;
		ObjectInputStream ois = null;
		try {
			ServerSocket server = new ServerSocket(10005);
			System.out.println("클라이언트의 접속에 대기합니다");
			socket = server.accept();
			
			oos = new ObjectOutputStream(socket.getOutputStream());
			ois = new ObjectInputStream(socket.getInputStream());
			
			Object obj = null;
			while ((obj = ois.readObject()) != null) {
				SendData sd = (SendData) obj;
				int op1 = sd.getOp1();
				int op2 = sd.getOp2();
				String opcode = sd.getOpcode();
				if (opcode.equals("+")) {
					oos.writeObject(String.valueOf(op1 + op2));
					oos.flush();
				} else if (opcode.equals("-")) {
					oos.writeObject(String.valueOf(op1 - op2));
					oos.flush();
				} else if (opcode.equals("*")) {
					oos.writeObject(String.valueOf(op1 * op2));
					oos.flush();
				} else if (opcode.equals("/")) {
					if (op2 == 0) {
						oos.writeObject("0으로 나눌 수 없습니다");
						oos.flush();
					} else {
						oos.writeObject(String.valueOf(op1 / op2));
						oos.flush();
					}
				}
				System.out.println("결과를 전송했습니다");
			}
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			try {
				if (oos != null) {
					oos.close();
				}
			} catch (Exception e) {
				
			}
			try {
				if (ois != null) {
					ois.close();
				}
			} catch (Exception e) {
				
			}
			try {
				if (socket != null) {
					socket.close();
				}
			} catch (Exception e) {
			}
		}
	}
}