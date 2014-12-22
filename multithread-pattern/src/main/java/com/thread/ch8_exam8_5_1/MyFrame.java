package com.thread.ch8_exam8_5_1;

import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.lang.reflect.InvocationTargetException;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.SwingUtilities;

public class MyFrame extends JFrame implements ActionListener {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private final JLabel label = new JLabel("button");
	private final JButton button = new JButton("countUp");

	public MyFrame() {
		super("MyFrame");
		getContentPane().setLayout(new FlowLayout());
		getContentPane().add(label);
		getContentPane().add(button);
		button.addActionListener(this);
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		pack();
		setVisible(true);
	}

	public void actionPerformed(ActionEvent e) {
		if (e.getSource() == button) {
			countUp();
		}
	}

	private void countUp() {
		System.out.println(Thread.currentThread().getName() + " countUp begin");
		new Thread() {
			public void run() {
				System.out.println(Thread.currentThread().getName()
						+ "invokerThread begin");
				for (int i = 0; i < 10; i++) {
					final String string = "" + i;
					final Runnable executor = new Runnable() {
						public void run() {
							System.out.println(Thread.currentThread().getName()
									+ " executor begin string=" + string);
							label.setText(string);
							System.out.println(Thread.currentThread().getName()
									+ "executor end");
						}
					};

					SwingUtilities.invokeLater(executor);
					try {
						Thread.sleep(1000);
					} catch (InterruptedException e) {
						e.printStackTrace();
					}
				}
				System.out.println(Thread.currentThread().getName()
						+ "invokerThread end");
			}
		}.start();
		System.out.println(Thread.currentThread().getName() + " countUp end");
	}
}
