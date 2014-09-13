package com.netty.elevator.frame;

import java.util.HashMap;
import java.util.Map;

/**
 * 
 * Composite Pattern (Leaf Class)
 * 
 * @author u2waremanager@gmail.com
 */
public abstract class FrameItem<V> extends Frame<V> {

	private Map<V, Object> messageMap = new HashMap<V, Object>();

	public void addMessage(V value, Object message) {
		messageMap.put(value, message);
	}

	public void removeMessage(V value) {
		messageMap.remove(value);
	}

	public Object getMessage() {
		if (getValue() != null && messageMap.containsKey(getValue())) {
			return messageMap.get(getValue());
		}
		return getValue();
	}

	public void setMessage(Object message) {
		for (V value : messageMap.keySet()) {
			if (messageMap.get(value).equals(message)) {
				setValue(value);
				return;
			}
		}
		setValue((V) message);
	}
}
