package com.projava7nio2.ch01;

import java.io.IOException;
import java.nio.file.FileStore;
import java.nio.file.FileSystem;
import java.nio.file.FileSystems;
import java.nio.file.Files;
import java.nio.file.LinkOption;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.attribute.AclFileAttributeView;
import java.nio.file.attribute.BasicFileAttributeView;
import java.nio.file.attribute.BasicFileAttributes;
import java.nio.file.attribute.FileOwnerAttributeView;
import java.nio.file.attribute.FileTime;
import java.nio.file.attribute.PosixFileAttributeView;
import java.util.Set;

public class Ch02 {

	public static void main(String[] args) throws IOException {
		// view supported view
		FileSystem fs = FileSystems.getDefault();
		Set<String> views = fs.supportedFileAttributeViews();
		for (String view : views) {
			System.out.println(view);
		}

		Path path = Paths.get("c:/", "a", "b", "c", "BNP.txt");
		System.out.println(path);

		// check supported view
		FileStore store = Files.getFileStore(path);
		boolean supported = store.supportsFileAttributeView("basic");
		System.out.println(supported);

		// get attribute
		BasicFileAttributes attr = Files.readAttributes(path,
				BasicFileAttributes.class);
		System.out.println(attr.lastModifiedTime());
		System.out.println(Files.getAttribute(path, "lastModifiedTime",
				LinkOption.NOFOLLOW_LINKS));

		// update basic attribute
		FileTime fileTime = FileTime.fromMillis(System.currentTimeMillis());
		Files.getFileAttributeView(path, BasicFileAttributeView.class)
				.setTimes(fileTime, fileTime, fileTime);
		System.out.println(Files.getAttribute(path, "lastModifiedTime",
				LinkOption.NOFOLLOW_LINKS));

		fileTime = FileTime.fromMillis(System.currentTimeMillis());
		System.out.println(Files.getAttribute(path, "lastModifiedTime",
				LinkOption.NOFOLLOW_LINKS));

		Files.setLastModifiedTime(path, fileTime);
		System.out.println(Files.getAttribute(path, "lastModifiedTime",
				LinkOption.NOFOLLOW_LINKS));

		fileTime = FileTime.fromMillis(System.currentTimeMillis());
		Files.setAttribute(path, "basic:lastModifiedTime", fileTime,
				LinkOption.NOFOLLOW_LINKS);

		fileTime = FileTime.fromMillis(System.currentTimeMillis());
		System.out.println(Files.getAttribute(path, "lastModifiedTime",
				LinkOption.NOFOLLOW_LINKS));

		// get fileownerattribute
		FileOwnerAttributeView fileattr = Files.getFileAttributeView(path,
				FileOwnerAttributeView.class);
		System.out.println(fileattr);

		// get posixfileattribute
		PosixFileAttributeView posixattr = Files.getFileAttributeView(path,
				PosixFileAttributeView.class);
		System.out.println(posixattr);

		AclFileAttributeView aclattr = Files.getFileAttributeView(path,
				AclFileAttributeView.class);
		System.out.println(aclattr);
		
		// get filesystem information
		for (FileStore stores : fs.getFileStores()) {
			System.out.println(stores.type());
			System.out.println(stores.getTotalSpace());
		}
	}
}
