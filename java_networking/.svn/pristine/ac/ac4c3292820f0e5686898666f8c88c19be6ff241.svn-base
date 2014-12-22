package com;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

public class Exam14_9CopyIOTest extends MyTimer {

	/**
	 * @param args
	 * @throws IOException 
	 */
	public static void main(String[] args) throws IOException {
		// TODO Auto-generated method stub
		start();
		copyIO();
		end("mp3.mp3");
	}

	private static void copyIO() throws IOException {
		FileInputStream fis = new FileInputStream(filePath);
		FileOutputStream fos = new FileOutputStream("mp3.mp3");
		
		byte[] buf = new byte[fis.available()];
		try {
			fis.read(buf);
			fos.write(buf);
		} finally {
			if (null != fis) {
				fis.close();
			}
			if (null != fos) {
				fos.close();
			}
		}
		
	}

}
