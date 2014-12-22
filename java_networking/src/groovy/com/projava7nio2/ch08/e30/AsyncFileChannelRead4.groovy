package com.projava7nio2.ch08.e30

import java.nio.ByteBuffer
import java.nio.channels.AsynchronousFileChannel;
import java.nio.channels.CompletionHandler
import java.nio.charset.Charset
import java.nio.file.Path
import java.nio.file.Paths
import java.nio.file.StandardOpenOption;

/**
 * completionHandler안에서 bytebuffer 접근하는 예제
 * @author cuckoo03
 *
 */
class AsyncFileChannelRead4 {
	static Thread current
	static final Path path = Paths.get("c:/home/exam", "Text.txt")

	static main(args) {
		CompletionHandler<Integer, ByteBuffer> handler =
				new CompletionHandler<Integer, ByteBuffer>() {
					String encoding = System.getProperty("file.encoding")

					@Override
					public void completed(Integer result, ByteBuffer attachment) {
						println "Read bytes:" + result
						attachment.flip()
						//printf Charset.forName(encoding).decode(attachment)
						attachment.clear()
						current.interrupt()
					}
					@Override
					public void failed(Throwable exc, ByteBuffer attachment) {
						println attachment
						println "Error:" + exc
						current.interrupt()
					}
				};

		AsynchronousFileChannel asynchronousFileChannel = null
		try {
			asynchronousFileChannel =
					AsynchronousFileChannel.open(path, StandardOpenOption.READ)
			current = Thread.currentThread()
			ByteBuffer buffer = ByteBuffer.allocate(100)
			asynchronousFileChannel.read(buffer, 0, buffer, handler)

			println "Waiting for reading operation to end.."
			try {
				current.join()
			} catch (InterruptedException e) {
			}

			println "\n\nClosing everything and leave"
		} catch (Exception e) {
			println e
		}
	}
}
