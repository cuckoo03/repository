package com.hadoop.cloudcomputing.ch05;

import java.io.IOException;

import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.fs.FSDataOutputStream;
import org.apache.hadoop.fs.FileSystem;
import org.apache.hadoop.fs.Path;

/**
 * hdfs append test cdh3 or hadoop0.21, 1.x.x에서는 아래의 옵션 추가필요 cdh3:hdfs-site.xml에
 * dfs.support.broken.append=true 속성추가 그외:hdfs-site.xml에 dfs.support.append=true
 * 속성추가
 * 
 * @author cuckoo03
 *
 */
public class AppendTest {
	public void startTest(String filePath) throws IOException {
		Configuration conf = new Configuration();
		FileSystem fs = FileSystem.get(conf);

		Path path = new Path(filePath);

		if (fs.exists(path)) {
			fs.delete(path, true);
		}

		FSDataOutputStream out1 = fs.create(path);
		out1.writeBytes("Write\n");
		out1.close();

		FSDataOutputStream out2 = fs.append(path);
		out2.writeBytes("Append\n");
		out2.close();

		if (fs.exists(path)) {
			fs.delete(path, true);
		}

		// 파일시스템에 대한 일관성(파일에 대한 읽기와 쓰기의 데이터 가시성)
		// 파일생성시 파일시스템의 네임스페이스에서 파일을 볼수 있음
		FSDataOutputStream out3 = fs.create(path);
		System.out.println("fs exists:" + fs.exists(path));
		out3.writeBytes("Write\n");

		// 스트림이 플러시되었다 할지라도 ㅍ일에 쓰여진 내용도 볼수있다고 장담할수는 없다 따라서 길이는 0으로 나타난다.
		out3.flush();
		// size:0
		System.out.println("file length:" + fs.getFileStatus(path).getLen());
		out3.close();
		// size:6
		System.out.println("file length:" + fs.getFileStatus(path).getLen());
		
		if (fs.exists(path)) {
			fs.delete(path, true);
		}

		FSDataOutputStream out4 = fs.create(path);
		System.out.println("fs exists:" + fs.exists(path));
		out4.write("Write\n".getBytes("UTF-8"));

		// sync() 메서드를 ㄷ통하여 모든 버퍼가 네임노드에 동기화 되도록 보장한다.
		// hadoop 0.20-cdh3u6에서는 여전히 sync()후에 사이지는 0으로 표시 
		out4.flush();
		out4.sync();
		// size:0
		System.out.println("sync file length:" + fs.getFileStatus(path).getLen());
		// hdfs의 파일을 닫는 것은 암묵적으로 sync()를 수행하는 것
		out4.close();
		// size:6
		System.out.println("sync file length:" + fs.getFileStatus(path).getLen());
	}

	public static void main(String[] args) throws IOException {
		if (args.length != 1) {
			System.out.println("Arguments required.");
			System.exit(1);
		}
		AppendTest instance = new AppendTest();
		instance.startTest(args[0]);
	}
}