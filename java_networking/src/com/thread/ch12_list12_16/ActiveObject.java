package com.thread.ch12_list12_16;

import java.util.concurrent.Future;

public interface ActiveObject {
	public abstract void displayString(String string);

	public abstract void shutdown();

	public abstract Future<String> makeString(int i, char fillchar);

}
