package com;

import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URI;
import java.net.URL;

public class Exam10_2WebSpider {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		URL url = null;
		FileOutputStream fos = null;
		try {
			url = new URL("http://www.yahoo.com");
			InputStream is = url.openStream();
			fos = new FileOutputStream("Exam10_2_file.html");
			
			byte[] buffer = new byte[is.available()];
			System.out.println("read start");
			int readcount = 0;
			while ((readcount = is.read(buffer)) != -1) {
				fos.write(buffer, 0, readcount);
			}
			System.out.println("read end");
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			if (fos != null) {
				try {
					fos.close();
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		}
	}

}
