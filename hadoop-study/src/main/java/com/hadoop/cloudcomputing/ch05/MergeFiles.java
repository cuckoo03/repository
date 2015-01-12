package com.hadoop.cloudcomputing.ch05;

import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;

import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.fs.FileStatus;
import org.apache.hadoop.fs.FileSystem;
import org.apache.hadoop.fs.FileUtil;
import org.apache.hadoop.fs.Path;
import org.apache.hadoop.util.GenericOptionsParser;

/**
 * hdfs 내의 작은 파일들을 하나의 파일로 모아주는 코드
 * 
 * @author cuckoo03
 *
 */
public class MergeFiles {
	public static long fileSize(Path path, Configuration conf)
			throws IOException {
		long retval = 0;
		FileSystem fs = path.getFileSystem(conf);
		FileStatus status = fs.getFileStatus(path);

		if (status.isDir()) {
			FileStatus[] fileListStatus = fs.listStatus(path);
			for (FileStatus fileStatus : fileListStatus) {
				retval += fileSize(fileStatus.getPath(), conf);
			}
		} else {
			retval += status.getLen();
		}
		return retval;
	}

	public static void main(String[] args) throws IOException {
		Configuration conf = new Configuration();
		String[] otherArgs = new GenericOptionsParser(conf, args)
				.getRemainingArgs();
		if (otherArgs.length != 2) {
			System.err.println("Usage <input dir> <output file name>");
			System.exit(1);
		}
		Path srcDir = new Path(otherArgs[0]);
		Path dstDir = new Path(otherArgs[1]);
		FileSystem srcFS = null;
		FileSystem dstFS = null;

		try {
			srcFS = FileSystem.get(new URI(otherArgs[0]), conf);
			dstFS = FileSystem.get(new URI(otherArgs[1]), conf);
		} catch (URISyntaxException e) {
			System.err.println("Could not load URI:");
			e.printStackTrace();
			System.exit(1);
		} catch (IOException e) {
			System.err.println("Colud not get FileSystem:");
			e.printStackTrace();
			System.exit(1);
		}

		try {
			boolean copySuccess = FileUtil.copyMerge(srcFS, srcDir, dstFS,
					dstDir, false, conf, null);
			if (copySuccess) {
				System.out.println("copy successed");
			} else {
				System.out.println("copy failed");
			}
		} catch (IOException e) {
			System.err.println("copy merge failed:" + e.getMessage());
			System.exit(1);
		}

		long inbytes = fileSize(srcDir, conf);
		long outbytes = fileSize(dstDir, conf);
		System.out.println("Read in:" + Long.toString(inbytes) + "bytes");
		System.out.println("Merged:" + Long.toString(outbytes) + "bytes");
		if (inbytes != outbytes) {
			System.err.println("input and output file sizes different");
		}
	}
}