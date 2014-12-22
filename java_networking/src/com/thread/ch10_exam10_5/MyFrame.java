package com.thread.ch10_exam10_5;

import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;

public class MyFrame extends JFrame implements ActionListener {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private final JButton executeButton = new JButton("execute");
	private final JButton cancelButton = new JButton("cancel");

	public MyFrame() {
		super("MyFrame");
		getContentPane().setLayout(new FlowLayout());
		getContentPane().add(new JLabel("two phase termination smaple"));
		getContentPane().add(executeButton);
		getContentPane().add(cancelButton);
		executeButton.addActionListener(this);
		cancelButton.addActionListener(this);
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		pack();
		setVisible(true);
	}

	public void actionPerformed(ActionEvent e) {
		if (e.getSource() == executeButton) {
			Service.service();
		} else if (e.getSource() == cancelButton) {
			Service.cancel();
		}
	}

}
