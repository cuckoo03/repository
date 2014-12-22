package com;

import java.awt.BorderLayout;
import java.awt.CardLayout;
import java.awt.DisplayMode;
import java.awt.Frame;
import java.awt.Label;
import java.awt.Panel;
import java.awt.TextArea;
import java.awt.TextField;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.net.Socket;

public class Exam8_9_Client extends Frame implements ActionListener {
	private static final String NORTH = "North";
	private static final String CENTER = "Center";
	private static final String SOUTH = "South";

	private TextField idTF = null;
	private TextField input = null;
	private TextArea display = null;
	private CardLayout cardLayout = null;

	private BufferedReader br = null;
	private PrintWriter pw = null;
	private Socket socket = null;

	public Exam8_9_Client(String ip) {
		super("채팅클라이언트");
		cardLayout = new CardLayout();
		setLayout(cardLayout);

		Panel loginPanel = new Panel();
		loginPanel.setLayout(new BorderLayout());
		loginPanel.add(NORTH, new Label("아이디를 입력하여 주신 후 엔터 키를 입력해서 주세요"));
		idTF = new TextField(20);
		idTF.addActionListener(this);
		Panel c = new Panel();
		c.add(idTF);
		loginPanel.add(CENTER, c);
		add("login", loginPanel);
		Panel main = new Panel();
		main.setLayout(new BorderLayout());
		input = new TextField();
		input.addActionListener(this);
		display = new TextArea();
		display.setEditable(false);
		main.add(CENTER, display);
		main.add(SOUTH, input);
		add("main", main);

		try {
			socket = new Socket(ip, 10001);
			pw = new PrintWriter(new OutputStreamWriter(socket
					.getOutputStream()));
			br = new BufferedReader(new InputStreamReader(socket
					.getInputStream()));
		} catch (Exception e) {
			System.out.println("서버와 접속시 오류가 발생했습니다");
			System.out.println(e);
			System.exit(1);
		}
		setSize(500, 500);
		cardLayout.show(this, "login");

		addWindowListener(new WindowAdapter() {
			public void windowClosing(WindowEvent e) {
				pw.println("/quit");
				pw.flush();
				try {
					socket.close();
				} catch (Exception exception) {
					System.out.println("종료합니다");
					System.out.println(0);
				}
			}
		});
		setVisible(true);
	}

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public void actionPerformed(ActionEvent e) {
		// TODO Auto-generated method stub
		if (e.getSource() == idTF) {
			String id = idTF.getText();
			if (id == null || id.trim().equals("")) {
				System.out.println("아이디를 다시 입력해 주세요");
				return;
			}
			pw.println(id.trim());
			pw.flush();
			WinInputThread wit = new WinInputThread(socket, br);
			wit.start();
			cardLayout.show(this, "main");
			input.requestFocus();
		} else if (e.getSource() == input) {
			String msg = input.getText();
			pw.println(msg);
			pw.flush();
			if (msg.equals("/quit")) {
				try {
					socket.close();
				} catch (Exception ex) {
					System.out.println("종료합니다");
					System.out.println(1);
				}
				input.setText("");
				input.requestFocus();
			}
		}
	}

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		new Exam8_9_Client("127.0.0.1");
	}

	class WinInputThread extends Thread {
		private Socket socket = null;
		private BufferedReader br = null;

		public WinInputThread(Socket socket, BufferedReader br) {
			this.socket = socket;
			this.br = br;
		}

		public void run() {
			try {
				String line = null;
				while ((line = br.readLine()) != null) {
					display.append(line + "\n");
				}
			} catch (Exception e) {
				try {
					if (br != null) {
						br.close();
					}
					if (socket != null) {
						socket.close();
					}
				} catch (Exception e2) {
					e.printStackTrace();
				}
			}
		}
	}
}
