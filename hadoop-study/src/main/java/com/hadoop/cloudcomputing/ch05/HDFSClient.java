package com.hadoop.cloudcomputing.ch05;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.fs.FileStatus;
import org.apache.hadoop.fs.FileSystem;
import org.apache.hadoop.fs.Path;

public class HDFSClient {
	public static void main(String[] args) throws IOException {
		Configuration conf = new Configuration();
		System.out.println("fs.default.name:" + conf.get("fs.default.name"));
		conf.set("fs.default.name", "hdfs://192.168.1.101:9000");
		FileSystem fs = FileSystem.get(conf);

		// create dir
		String dirName = "TestDirectory";
		System.out
				.println("working dir:" + fs.getWorkingDirectory().toString());
		Path src = new Path(fs.getWorkingDirectory() + "/" + dirName);
		fs.mkdirs(src);

		// copy hdfs from local's file
		Path localFileSrc = new Path("/usr/local/hadoop/README.txt");
		fs.copyFromLocalFile(localFileSrc, localFileSrc);

		// copy local from hdfs's file
		fs.copyToLocalFile(src, localFileSrc);

		// delete dir
		fs.delete(src, true);

		// write data
		fs.mkdirs(src);
		Path fileSrc = new Path(src, "/test01.dat");
		OutputStream os = fs.create(fileSrc);
		os.write("this is test".getBytes());
		os.close();

		// read data
		InputStream is = fs.open(fileSrc);
		byte[] readBuf = new byte[1024];
		int readBytes;
		while ((readBytes = is.read(readBuf)) > 0) {
			System.out.println(new String(readBuf, 0, readBytes));
		}

		System.out.println("Exists: " + fs.exists(src));
		FileStatus fileStatus = fs.getFileStatus(src);
		System.out.println("IsDir:" + fileStatus.isDir());
		System.out.println("List Count:" + fs.listStatus(src).length);

		System.out.println("BlockSize:" + fs.getDefaultBlockSize());
		System.out.println("Replica:' + fs" + fs.getDefaultReplication());
	}
}
