package com;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.SocketException;
import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

public class Exam8_8 {

	/**
	 * 여러 개의 방을 구현한 예제
	 * 
	 * @param args
	 */
	public static void main(String[] args) {
		ServerSocket server = null;
		Map<String, Map<String, PrintWriter>> rooms = null;
		try {
			server = new ServerSocket(10001);
			System.out.println("사용자 접속을 기다립니다.");
			rooms = new HashMap<String, Map<String,PrintWriter>>();
			while (true) {
				Socket socket = server.accept();
				ChatThread2 chatthread = new ChatThread2(socket, rooms);
				chatthread.start();
			}
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			try {
				server.close();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}
}

class ChatThread2 extends Thread {
	private Socket socket;
	private String id;
	private BufferedReader br;
	private Map<String, PrintWriter> hm;
	private boolean initFlag = false;

	public ChatThread2(Socket socket,
			Map<String, Map<String, PrintWriter>> rooms) {
		this.socket = socket;
		try {
			PrintWriter pw = new PrintWriter(new OutputStreamWriter(socket
					.getOutputStream()));
			br = new BufferedReader(new InputStreamReader(socket
					.getInputStream()));

			String roomNum = br.readLine();
			if (rooms.containsKey(roomNum)) {
				this.hm = rooms.get(roomNum);
				System.out.println("방에 입장하였습니다.");
			} else {
				this.hm = new HashMap<String, PrintWriter>();
				rooms.put(roomNum, hm);
				System.out.println("방이 생성되었습니다");
			}
			
			id = br.readLine();
			broadcast(id + "님이 접속 하였습니다.");
			System.out.println("접속한 사용자의 아이디는" + id);

			synchronized (hm) {
				hm.put(id, pw);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public void run() {
		try {
			String line = null;
			while ((line = br.readLine()) != null) {
				if (line.equals("/quit")) {
					break;
				}
				if (line.indexOf("/to") == 0) {
					sendmsg(line);
				} else {
					broadcast(id + ">" + line);
				}
			}
		} catch (IOException e) {
			System.out.println("사용자 접속이 끊겼습니다");
		} finally {
			synchronized (hm) {
				hm.remove(id);
			}
			broadcast(id + "님이 접속 종료했습니다.");
			try {
				if (socket != null) {
					socket.close();
				}
			} catch (Exception e) {
				System.out.println(e);
			}
		}
	}

	private void broadcast(String msg) {
		synchronized (hm) {
			Collection<PrintWriter> collection = hm.values();
			Iterator<PrintWriter> iter = collection.iterator();
			while (iter.hasNext()) {
				PrintWriter pw = iter.next();
				pw.println(msg);
				pw.flush();
			}
		}
	}

	private void sendmsg(String msg) {
		int start = msg.indexOf(" ") + 1;
		int end = msg.indexOf(" ", start);

		if (-1 != end) {
			String to = msg.substring(start, end);
			String msg2 = msg.substring(end + 1);
			PrintWriter pw = hm.get(to);
			if (null != pw) {
				pw.println(id + "님이 다음의 귓속말을 보냈습니다:" + msg2);
				pw.flush();
			}
		}
	}
}
