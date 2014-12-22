package com.thread.ch12_exam12_2_2;

import java.util.concurrent.Future;

public interface ActiveObject {
	public abstract void shutdown();

	public abstract Future<String> add(String x, String y);

}
