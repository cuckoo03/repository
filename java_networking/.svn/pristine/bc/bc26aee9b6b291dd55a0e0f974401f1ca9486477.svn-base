package com.thread.ch12_list12_16;

import java.util.concurrent.Callable;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

public class ActiveObjectImpl implements ActiveObject {
	private ExecutorService service = Executors.newSingleThreadExecutor();

	public void displayString(final String string) {
		class DisplayStringRequest implements Runnable {
			public void run() {
				try {
					System.out.println("displayString: " + string);
					Thread.sleep(10);
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
			}
		}

		service.execute(new DisplayStringRequest());
	}

	public void shutdown() {
		service.shutdown();
	}

	public Future<String> makeString(final int count, final char fillchar) {
		class MakeStringRequest implements Callable<String> {
			public String call() throws Exception {
				char[] buffer = new char[count];
				for (int i = 0; i < count; i++) {
					buffer[i] = fillchar;

					Thread.sleep(100);
				}

				return new String(buffer);
			}
		}
		return service.submit(new MakeStringRequest());
	}
}