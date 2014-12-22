package com;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

public class Exam12_3FullBuffer {
	/**
	 * 파일 크기만큼 버퍼로  사용 예제
	 * @param args
	 */
	public static void main(String[] args) {
		long start = System.currentTimeMillis();
		
		copy("mp3.mp3", "mp4.mp3");
		long end = System.currentTimeMillis() - start;
		System.out.println("time:" + end);
	}
	
	public static void copy(String filename1, String filename2) {
		InputStream is = null;
		OutputStream os = null;
		
		try {
			is = new FileInputStream(filename1);
			os = new FileOutputStream(filename2);
			
			int available = is.available();
			byte[] buffer = new byte[available];
			int data = is.read(buffer);
			os.write(buffer);
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			try {
				os.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
			try {
				is.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}
}
