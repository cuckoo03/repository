package com.hadoop.log.merge;

import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.text.SimpleDateFormat;
import java.util.Calendar;

import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.fs.FileStatus;
import org.apache.hadoop.fs.FileSystem;
import org.apache.hadoop.fs.FileUtil;
import org.apache.hadoop.fs.Path;
import org.apache.hadoop.util.GenericOptionsParser;

/**
 * 하루 주기로 저장된 파일들을 머지하는 프로그램 지정된 디렉토리에 yyyy-MM-dd 형태로 파일을 머지하여 생성한다.
 * 
 * @author cuckoo03
 *
 */
public class DailyFilesMerge {
	private static final SimpleDateFormat sdf1 = new SimpleDateFormat(
			"yyyy-MM-dd");

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

	public static String getCurrentDate() {
		Calendar cal = Calendar.getInstance();
		String dateStr = sdf1.format(cal.getTime());
		return dateStr;
	}

	public void run(String[] otherArgs, Configuration conf) throws IOException {
		String dateDir = getCurrentDate();
		System.out.println("-------------------------");
		System.out.println(otherArgs[1]);
		System.out.println("-------------------------");
		String srcDir = otherArgs[0] + "/" + dateDir;
		Path srcDirPath = new Path(srcDir);

		String dstFile = otherArgs[0]+ "/" + otherArgs[1];
		Path dstFilePath = new Path(dstFile);

		FileSystem srcFS = null;
		FileSystem dstFS = null;

		try {
			srcFS = FileSystem.get(new URI(srcDir), conf);
			dstFS = FileSystem.get(new URI(dstFile), conf);

			// 기존 존재하는 파일 삭제
			dstFS.delete(dstFilePath, true);
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
			boolean copySuccess = FileUtil.copyMerge(srcFS, srcDirPath, dstFS,
					dstFilePath, false, conf, null);
			if (copySuccess) {
				System.out.println("copy successed");
			} else {
				System.out.println("copy failed");
			}
		} catch (IOException e) {
			System.err.println("copy merge failed:" + e.getMessage());
			System.exit(1);
		}

		long inbytes = fileSize(srcDirPath, conf);
		long outbytes = fileSize(dstFilePath, conf);
		System.out.println("Read in:" + Long.toString(inbytes) + "bytes");
		System.out.println("Merged:" + Long.toString(outbytes) + "bytes");
		if (inbytes != outbytes) {
			System.err.println("input and output file sizes different");
		}
	}

	/**
	 * input dir:로그가 저장된 디렉토리
	 * 
	 * @param args
	 * @throws IOException
	 */
	public static void main(String[] args) throws IOException {
		Configuration conf = new Configuration();
		String[] otherArgs = new GenericOptionsParser(conf, args)
				.getRemainingArgs();
		if (otherArgs.length != 2) {
			System.err.println("Usage <input dir> <yyyy-mm-dd>");
			System.exit(1);
		}

		DailyFilesMerge merge = new DailyFilesMerge();
		merge.run(otherArgs, conf);
	}
}