package com.netty.buffer;

import com.netty.common.DataChangeEventListener;

public class DataObserver implements DataChangeEventListener {
	@Override
	public void dataChanged() {
		System.out.println("dataChanged");
	}
}
