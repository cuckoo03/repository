package com;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

public class Exam12_2NonBuffer {

	/**
	 * @param args
	 * @throws IOException 
	 */
	public static void main(String[] args) throws IOException {
		long start = System.currentTimeMillis();
		copy("mp3.mp3", "mp4.mp3");
		long end = System.currentTimeMillis() - start;
		System.out.println("time:" + end);
	}

	private static void copy(String filename1, String filename2) throws IOException {
		InputStream is = null;
		OutputStream os = null;
		BufferedInputStream bis = null;
		BufferedOutputStream bos = null;
		try {
			is = new FileInputStream(filename1);
			bis = new BufferedInputStream(is, 2048);
			os = new FileOutputStream(filename2);
			bos = new BufferedOutputStream(os, 2048);

			/*
			while (true) {
				int read = bis.read();
				if (-1 == read) {
					break;
				}

				bos.write(read);
			}
			*/
			
			while (true) {
				int read = is.read();
				if (-1 == read) {
					break;
				}
				os.write(read);
			}
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			is.close();
			bis.close();
			bis.close();
			bos.close();
		}
	}

}
