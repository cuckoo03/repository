package com;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;

public class Exam8_7 {

	/**
	 * @param args
	 * @throws IOException
	 */
	public static void main(String[] args) {
		try {
			ServerSocket server = new ServerSocket(10001);
			System.out.println("접속을 기다립니다");
			HashMap<String, PrintWriter> hm = new HashMap<String, PrintWriter>();
			while (true) {
				Socket socket = server.accept();
				ChatThread chatthread = new ChatThread(socket, hm);
				chatthread.start();
			}
		} catch (IOException e) {
			System.out.println(e);
		}
	}

}

class ChatThread extends Thread {
	private Socket socket;
	private String id;
	private BufferedReader br;
	private HashMap<String, PrintWriter> hm;
	private boolean initFlag = false;

	public ChatThread(Socket socket, HashMap<String, PrintWriter> hm) {
		this.socket = socket;
		this.hm = hm;
		try {
			PrintWriter pw = new PrintWriter(new OutputStreamWriter(socket
					.getOutputStream()));
			br = new BufferedReader(new InputStreamReader(socket
					.getInputStream()));
			id = br.readLine();

			broadcast(id + "님이 접속했습니댜");
			System.out.println("접속한 사용자의 아이디는" + id);
			synchronized (hm) {
				hm.put(this.id, pw);
			}
			initFlag = true;
		} catch (Exception e) {
			System.out.println(e);
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
					broadcast(id + ":" + line);
				}
			}
		} catch (Exception e) {
			System.out.println(e);
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