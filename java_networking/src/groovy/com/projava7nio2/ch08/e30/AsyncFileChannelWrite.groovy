package com.projava7nio2.ch08.e30

import java.nio.ByteBuffer
import java.nio.channels.AsynchronousFileChannel
import java.nio.file.Path
import java.nio.file.Paths
import java.nio.file.StandardOpenOption
import java.util.concurrent.Future

/**
 * 파일 쓰기와 future
 * @author cuckoo03
 *
 */
class AsyncFileChannelWrite {
	static main(args) {
		ByteBuffer buffer = ByteBuffer.wrap("ABC ".getBytes())
		Path path = Paths.get("c:/home/exam", "text.txt")
		AsynchronousFileChannel asynchronousFileChannel = null

		try {
			asynchronousFileChannel =
					AsynchronousFileChannel.open(path, StandardOpenOption.WRITE)
			
			Future<Integer> result = asynchronousFileChannel.write(buffer, 100)
			
			while (!result.isDone()) {
				println "Do something else while writing..."
			}
			
			println "Written done:" + result.isDone()
			println "Bytes written:" + result.get()
		} catch (Exception e) {
			println e
		}
	}
}
