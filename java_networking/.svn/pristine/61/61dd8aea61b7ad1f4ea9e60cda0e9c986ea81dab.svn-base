package com;

import java.io.InputStream;
import java.io.OutputStream;
import java.io.PipedInputStream;
import java.io.PipedOutputStream;

class ReadThread extends Thread implements Runnable {
	InputStream pi = null;
	OutputStream po = null;
	ReadThread(InputStream pi, OutputStream po) {
		this.pi = pi;
		this.po = po;
	}
	public void run() {
		int ch;
		byte[] buffer = new  byte[512];
		int readcount;
		try {
			for (;;) {
				readcount = pi.read(buffer);
				if (-1 == readcount) {
					return;
				}
				po.write(buffer, 0, readcount);
				System.out.println(readcount);
			}
		} catch (Exception e) {
			
		} finally {
			
		}
	}
}
public class Exam4_13 {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		// TODO Auto-generated method stub
		try {
			int ch;
			while (true) {
				PipedInputStream writeIn = new PipedInputStream();
				PipedOutputStream readOut = new PipedOutputStream(writeIn);
				ReadThread rt = new ReadThread(System.in, readOut);
				ReadThread wt = new ReadThread(writeIn, System.out);
				rt.start();
				wt.start();
				Thread.sleep(1000);
				
			}
		} catch (Exception e) {
			
		} finally {
			
		}
	}

}
