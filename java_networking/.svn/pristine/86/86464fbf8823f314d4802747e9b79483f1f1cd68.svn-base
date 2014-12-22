package com;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;

public class Exam8_11_Client {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		Socket socket = null;
		ObjectOutputStream oos = null;
		ObjectInputStream ois = null;
		try {
			socket = new Socket("127.0.0.1", 10005);

			oos = new ObjectOutputStream(socket.getOutputStream());
			ois = new ObjectInputStream(socket.getInputStream());

			BufferedReader br = new BufferedReader(new InputStreamReader(
					System.in));
			String line = null;

			while (true) {
				System.out.println("첫 번째 숫자를 입력하세요. 잘못 입력된 숫자는 0으로 처리합니다");
				line = br.readLine();
				int op1 = 0;
				try {
					op1 = Integer.parseInt(line);
				} catch (NumberFormatException nfe) {
					op1 = 0;
				}

				System.out.println("두번째 숫자를 입력해 주세요");
				line = br.readLine();
				int op2 = 0;
				try {
					op2 = Integer.parseInt(line);
				} catch (NumberFormatException nfe) {
					op2 = 0;
				}

				System.out.println("+=*/ 중에 하나를 입력하세요");
				line = br.readLine();
				String opcode = "+";
				if (line.equals("+") || line.equals("-") || line.equals("*")
						|| line.equals("/")) {
					opcode = line;
				} else {
					opcode = "+";
				}
				SendData s = new SendData(op1, op2, opcode);
				oos.writeObject(s);
				oos.flush();
				
				String msg = (String) ois.readObject();
				System.out.println(msg);
				System.out.println("계속 계산하시겠습니까");
				line = br.readLine();
				if (line.equals("n") || line.equals("N")) {
					break;
				}
				System.out.println("다시 계산을 시작합니다");
			}
			System.out.println("프로그램을 종료합니다");
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			try {
				if (oos != null)
				{
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
