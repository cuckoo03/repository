package com;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.FileInputStream;
import java.io.IOException;

public class Exam4_11 {

	/**
	 * @param args
	 * @throws IOException 
	 */
	public static void main(String[] args) throws IOException {
		// TODO Auto-generated method stub
		if (args.length != 1) {
			System.out.println("args length not 1");
			System.exit(0);
		}
		FileInputStream fis = null;
		ByteArrayInputStream bais = null;
		ByteArrayOutputStream baos = null;
		try {
			fis = new FileInputStream(args[0]);
			baos = new ByteArrayOutputStream();
			byte[] buffer = new byte[512];
			int readcount = 0;
			while ((readcount = fis.read(buffer)) != -1) {
				baos.write(buffer, 0, readcount);
				//baos.write(readcount);
			}
			
			byte[] filearray = baos.toByteArray();
			System.out.println("read byte:" + filearray.length);
			
			bais = new ByteArrayInputStream(filearray);
			while ((readcount = bais.read(buffer)) != -1) {
				System.out.write(buffer , 0, readcount);
			}
			System.out.println("\n\n");
			System.out.println("모두 출력했음");
		} catch (Exception e) {
			System.out.println(e.getMessage());
		} finally {
			bais.close();
			baos.close();
			fis.close();
		}
	}

}
