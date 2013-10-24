package org.apache.commons.io;

import java.io.IOException;

public class FileSystemUtilsMain {

	@SuppressWarnings("deprecation")
	public static void main(String[] args) throws IOException {
		long freespace = FileSystemUtils.freeSpace("c:/logs");
		
		System.out.println(freespace);
	}

}
