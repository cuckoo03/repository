package com.projava7nio2.ch01;

import java.io.IOException;
import java.nio.file.FileSystems;
import java.nio.file.Files;
import java.nio.file.Path;

public class Ch03 {

	public static void main(String[] args) {
		// create symbol link
		Path link = FileSystems.getDefault().getPath("d");
		Path target = FileSystems.getDefault().getPath("c:/a/b/c", "BNP.txt");

		try {
			Files.createSymbolicLink(link, target);

		} catch (IOException | SecurityException
				| UnsupportedOperationException e) {
			if (e instanceof SecurityException) {
				System.err.println("permission denied");
			}
			if (e instanceof UnsupportedOperationException) {
				System.err.println("unspported operation");
			}
			if (e instanceof IOException) {
				System.err.println("io error");
			}
			System.err.println(e);
		}
	}
}
