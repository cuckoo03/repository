package com.projava7nio2.ch08.e30

import java.nio.ByteBuffer
import java.nio.channels.AsynchronousFileChannel
import java.nio.charset.Charset;
import java.nio.file.Path
import java.nio.file.Paths
import java.nio.file.StandardOpenOption
import java.util.concurrent.Future

/**
 * 파일 읽기와 Future
 * @author cuckoo03
 *
 */
class AsyncFileChannelRead {
	static main(args) {
		ByteBuffer buffer = ByteBuffer.allocate(10)
		String encoding = System.getProperty("file.encoding")

		Path path = Paths.get("c:/home/exam", "text.txt")
		AsynchronousFileChannel asynchronousFileChannel = null

		try {
			asynchronousFileChannel =
					AsynchronousFileChannel.open(path, StandardOpenOption.READ)
			Future<Integer> result = asynchronousFileChannel.read(buffer, 0)
			
			while (!result.isDone()) {
				println "Do something else while reading..."
			}
			
			println "Read done:" + result.isDone()
			println "Bytes read:" + result.get()
		} catch (Exception e) {
			println e
		}
		
		buffer.flip()
		println Charset.forName(encoding).decode(buffer)
		buffer.clear()
	}
}
