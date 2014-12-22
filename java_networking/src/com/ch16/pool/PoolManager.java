package com.ch16.pool;

import java.util.HashMap;
import java.util.Map;

import com.ch16.pool.buffer.ByteBufferPoolIF;
import com.ch16.pool.selector.SelectorPoolIF;

public class PoolManager {

	private static Map<String, SelectorPoolIF> selectorMap = new HashMap<String, SelectorPoolIF>();
	private static Map<String, ByteBufferPoolIF> byteBufferMap = new HashMap<String, ByteBufferPoolIF>();

	private static PoolManager instance = null;

	private PoolManager() {
	}

	public static PoolManager getInstance() {
		if (null == instance) {
			instance = new PoolManager();
		}
		return instance;
	}

	public void registAcceptSelectorPool(SelectorPoolIF selectorPool) {
		selectorMap.put("AcceptSelectorPool", selectorPool);
	}

	public void registRequestSelectorPool(SelectorPoolIF selectorPool) {
		selectorMap.put("RequestSelectorPool", selectorPool);
	}

	public SelectorPoolIF getAcceptSelectorPool() {
		return selectorMap.get("AcceptSelectorPool");
	}

	public SelectorPoolIF getRequestSelectorPool() {
		return selectorMap.get("RequestSelectorPool");
	}

	public void registByteBufferPool(ByteBufferPoolIF byteBufferPool) {
		byteBufferMap.put("ByteBufferPool", byteBufferPool);
	}

	public ByteBufferPoolIF getByteBufferPool() {
		return byteBufferMap.get("ByteBufferPool");
	}
}
