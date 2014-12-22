package com;

import java.io.FileOutputStream;
import java.io.InputStream;
import java.net.URL;
import java.net.URLConnection;
import java.text.SimpleDateFormat;
import java.util.Date;

public class Exam10_3WebSpiderWithURLConnection {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		URL url = null;
		FileOutputStream fos = null;
		try {
			url = new URL("http://www.yahoo.com");
			URLConnection urlconn = url.openConnection();
			String contentType = urlconn.getContentType();
			long d1 = urlconn.getDate();
			Date d = new Date(d1);
			
			SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss a");
			String sdate = format.format(d);
			
			System.out.println("content type:" + contentType);
			System.out.println("time:" + sdate);
			
			InputStream is = urlconn.getInputStream();
			fos = new FileOutputStream("Exam10_3.html");
			
			byte[] buffer = new byte[is.available()];
			int readcount = 0;
			
			System.out.println("read start");
			while ((readcount = is.read(buffer)) != -1) {
				fos.write(buffer, 0, readcount);
			}
			System.out.println("read end");
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

}
