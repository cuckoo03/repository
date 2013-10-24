package org.apache.commons.io;

import java.io.File;
import java.io.IOException;
import java.util.Iterator;
import java.util.List;

public class FileUtilsMain {

	public static void main(String[] args) {
		// TODO Auto-generated method stub
		File file = new File("pom.xml");
		try {
			List lines = FileUtils.readLines(file, "UTF-8");
			Iterator iter = lines.iterator();
			while (iter.hasNext()) {
				System.out.println(iter.next());
			}
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

}
