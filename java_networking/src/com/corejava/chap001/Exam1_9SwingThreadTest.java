package com.corejava.chap001;

import java.awt.EventQueue;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.util.Random;

import javax.swing.DefaultListModel;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.JScrollPane;

public class Exam1_9SwingThreadTest {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		// TODO Auto-generated method stub
		JFrame frame = new SwingThreadFrame();
		frame.show();
	}

}

class SwingThreadFrame extends JFrame {
	public SwingThreadFrame() {
		setTitle("SwingThread");
		setSize(400, 300);
		addWindowListener(new WindowAdapter() {
			public void windowClosing(WindowEvent e) {
				System.exit(0);
			}
		});
		model = new DefaultListModel();
		JList list = new JList(model);
		JScrollPane scrollPane = new JScrollPane(list);
		JPanel p = new JPanel();
		p.add(scrollPane);
		getContentPane().add(p, "South");
		JButton b = new JButton("Good");
		b.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent event) {
				new GoodWorkerThread(model).start();
			}
		});
		p = new JPanel();
		p.add(b);
		b = new JButton("Bad");
		b.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent event) {
				new BadWorkerThread(model).start();
			}
		});
		p.add(b);
		getContentPane().add(p, "North");
	}

	private DefaultListModel model;
}

class BadWorkerThread extends Thread {
	public BadWorkerThread(DefaultListModel aModel) {
		model = aModel;
		generator = new Random();
	}

	public void run() {
		while (true) {
			Integer i = new Integer(generator.nextInt(10));
			if (model.contains(i))
				model.removeElement(i);
			else
				model.addElement(i);
			yield();
		}
	}

	private DefaultListModel model;
	private Random generator;
}

class GoodWorkerThread extends Thread {
	public GoodWorkerThread(DefaultListModel aModel) {
		model = aModel;
		generator = new Random();
	}

	public void run() {
		while (true) {
			final Integer i = new Integer(generator.nextInt(10));
			EventQueue.invokeLater(new Runnable() {
				public void run() {
					if (model.contains(i))
						model.removeElement(i);
					else
						model.addElement(i);
				}
			});
			yield();
		}
	}

	private DefaultListModel model;
	private Random generator;
}
