package com.projava7nio2.ch08.e30

import java.nio.ByteBuffer
import java.nio.channels.AsynchronousFileChannel;
import java.nio.channels.CompletionHandler;
import java.nio.file.Path
import java.nio.file.Paths
import java.nio.file.StandardOpenOption;


/**
 * 파일읽기와 CompletionHandler
 * @author cuckoo03
 *
 */
class AsyncFileChannelRead3 {
	static Thread current
	static main(args) {
		ByteBuffer buffer = ByteBuffer.allocate(100)
		Path path = Paths.get("c:/home/exam", "text.txt")

		AsynchronousFileChannel asynchronousFileChannel = null
		try {
			asynchronousFileChannel =
					AsynchronousFileChannel.open(path, StandardOpenOption.READ)

			current = Thread.currentThread()
			asynchronousFileChannel.read(buffer, 0, "Read operation status...",
					new CompletionHandler<Integer, Object>(){
						@Override
						public void completed(Integer result, Object attachment) {
							println attachment
							println "Read bytes:" + result
							current.interrupt()
						}
						@Override
						public void failed(Throwable exc, Object attachment) {
							println attachment
							println "Error:" + exc
							current.interrupt()
						}
					});

			println "\nWaiting for reading operation to end."
			try {
				current.join()
			} catch (InterruptedException e) {
				//				println e
			}

			println "\n\nClose everything and leave."
		} catch (Exception e) {
			println e
		}
	}
}
