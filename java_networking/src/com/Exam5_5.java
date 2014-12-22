package com;

import java.io.BufferedWriter;
import java.io.CharArrayReader;
import java.io.CharArrayWriter;
import java.io.FileReader;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;

public class Exam5_5 {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		// TODO Auto-generated method stub
		FileReader fis = null;
		CharArrayReader car = null;
		CharArrayWriter caw = null;
		try {
			fis = new FileReader("src/com/data.bin");
			caw = new CharArrayWriter();
			int readcount = 0;
			char[] buffer = new char[512]; 
			while ((readcount = fis.read(buffer)) != -1) {
				caw.write(buffer, 0, readcount);
			}
			char[] array = caw.toCharArray();
			System.out.println("length:" + array.length);
			
			car = new CharArrayReader(array);
			PrintWriter pw = new PrintWriter(System.out);
			BufferedWriter bw = new BufferedWriter(new OutputStreamWriter(System.out));
			while ((readcount = car.read(buffer)) != -1) {
				//bw.write(buffer, 0, readcount);
				pw.print(buffer);
				//bw.flush();
			}
			
			System.out.println("print");
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			
		}
	}

}
