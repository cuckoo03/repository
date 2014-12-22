package com;

import java.io.FileInputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;

public class Exam5_1 {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		// TODO Auto-generated method stub
		FileInputStream fis = null;
		InputStreamReader isr = null;
		OutputStreamWriter osw = null;
		try {
			fis = new FileInputStream("src/com/Exam4_11.java");
			isr = new InputStreamReader(fis);
			osw = new OutputStreamWriter(System.out);
			char[] buffer = new char[512];
			int readcount = 0;
			while ((readcount = isr.read(buffer)) != -1) {
				//System.out.println(new String(buffer));
				osw.write(buffer, 0, readcount);
			}
			
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			
		}
	}

}
