package com.projava7nio2.ch09.e31

import java.nio.ByteBuffer
import java.nio.channels.AsynchronousFileChannel;
import java.nio.file.Path
import java.nio.file.Paths
import java.nio.file.StandardOpenOption;
import java.util.concurrent.Future
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

/**
 * 파일 읽기와 Future 타임아웃
 * @author cuckoo03
 *
 */
class AsyncFileChannelRead2 {
	static main(args) {
		ByteBuffer buffer = ByteBuffer.allocate(100)
		int bytesRead = 0
		Future<Integer> result = null
		Path path = Paths.get("c:/home/exam", "text.txt")

		AsynchronousFileChannel asynchronousFileChannel = null
		try {
			asynchronousFileChannel =
					AsynchronousFileChannel.open(path, StandardOpenOption.READ)
			result = asynchronousFileChannel.read(buffer, 0)
			bytesRead = result.get(1, TimeUnit.NANOSECONDS)
			if (result.isDone()) {
				println "The result is available"
				println "Read bytes:" + bytesRead
			}
		} catch (Exception e) {
			if (e instanceof TimeoutException) {
				if (result != null) {
					result.cancel(true)
				}
				println "The result is not available"
				println "The read take was cancelled? " + result.isCancelled()
				println "Read bytes:" + bytesRead
			} else {
				println e
			}
		}
	}
}
