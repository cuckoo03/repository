package com.projava7nio2.ch01;

import java.io.File;
import java.io.IOException;
import java.nio.file.FileSystems;
import java.nio.file.Files;
import java.nio.file.LinkOption;
import java.nio.file.Path;
import java.nio.file.Paths;

public class Ch01 {

	public static void main(String[] args) throws IOException {
		Path path = Paths.get("c:/a/b/c/BNP.txt");

		Path nomalize = Paths.get(
				"c:/a/b/./c/dummy/../BNP.txt").normalize();

		path = FileSystems.getDefault().getPath("/a/b/c",
				"BNP.txt");

		System.out.println(path.toFile().isFile());
		System.out.println(nomalize.toFile().isFile());
		System.out.println(path.getFileName());
		System.out.println(path.getRoot());
		System.out.println(path.getParent());
		System.out.println(path.getNameCount());
		System.out.println(path.subpath(0, 3));
		System.out.println(path.toUri());
		System.out.println(path.toAbsolutePath());
		path = path.toRealPath(LinkOption.NOFOLLOW_LINKS);
		System.out.println(path);
		File file = path.toFile();
		System.out.println("file:" + file);
		System.out.println(path.equals(nomalize));
		System.out.println(Files.isSameFile(path, nomalize));
		System.out.println(path.compareTo(nomalize));
		for (Path name : path) {
			System.out.println(name);
		}
	}
}
