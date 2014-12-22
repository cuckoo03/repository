package com.thread.ch12_exam12_3;

import java.awt.BorderLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.SwingUtilities;

public class MyFrame extends JFrame implements ActionListener {
	private final JTextField textfield = new JTextField("word", 10);
	private final JButton button = new JButton("Search");
	private final JTextArea textare = new JTextArea(20, 30);
	private final ActiveObject activeObject = ActiveObjectFactory
			.createActiveObject();
	private final static String NEWLINE = System.getProperty("line.separator");

	public MyFrame() {
		super("ActiveObject sample");
		getContentPane().setLayout(new BorderLayout());

		JPanel north = new JPanel();
		north.add(new JLabel("search"));
		north.add(textfield);
		north.add(button);
		button.addActionListener(this);

		JScrollPane center = new JScrollPane(textare);

		getContentPane().add(north, BorderLayout.NORTH);
		getContentPane().add(center, BorderLayout.CENTER);

		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		pack();
		setVisible(true);
	}

	public void actionPerformed(final ActionEvent word) {
		final Result<String> result = activeObject.search(word);
		println("searching... " + word);
		new Thread() {
			public void run() {
				final String url = result.getResultValue();
				SwingUtilities.invokeLater(new Runnable() {
					public void run() {
						MyFrame.this.println("word=" + word + "url=" + url);
					}
				});
			}
		}.start();
	}

	private void println(String string) {
		textare.append(string + NEWLINE);
	}

}
