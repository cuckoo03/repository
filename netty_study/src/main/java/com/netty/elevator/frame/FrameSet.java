package com.netty.elevator.frame;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.beanutils.PropertyUtils;
import org.apache.commons.lang.ClassUtils;
import org.jboss.netty.buffer.ChannelBuffer;

/**
 * 
 * Composite Pattern (Compsite Class)
 * 
 * @author u2waremanager@gmail.com
 */
public abstract class FrameSet extends Frame {

	private Class beanClass;
	private List<Frame> childs = new ArrayList<Frame>();

	public FrameSet(String beanName) {
		try {
			beanClass = ClassUtils.getClass(beanName);
		} catch (ClassNotFoundException e) {
			throw new IllegalArgumentException("class not found: " + beanName);
		}
		super.setName(beanName);
	}

	public FrameSet(Class beanClass) {
		this.beanClass = beanClass;
		super.setName(beanClass.getName());
	}

	// //////////////////////////////////////////////
	//
	// //////////////////////////////////////////////
	public void addFrame(Frame child) {
		childs.add(child);
	}

	public void removeFrame(Frame child) {
		childs.remove(child);
	}

	public Frame getFrame(String name) {
		for (Frame child : childs) {
			if (child.getName().equals(name))
				return child;
		}
		return null;
	}

	// //////////////////////////////////////////////
	//
	// //////////////////////////////////////////////
	public int decode(ChannelBuffer buffer, int index) throws Exception {

		int newIndex = index;
		for (Frame child : childs) {
			newIndex = child.decode(buffer, newIndex);
		}

		Object newValue = beanClass.newInstance();
		String propertyName = null;
		Object propertyValue = null;
		for (Frame child : childs) {
			try {
				propertyName = child.getName();
				propertyValue = child.getMessage();
				PropertyUtils
						.setProperty(newValue, propertyName, propertyValue);

				// logger.debug("propertyName=" + propertyName +
				// ",propertyValue="
				// + propertyValue);
			} catch (Exception e) {
			}
		}
		super.setValue(newValue);

		return newIndex;
	}

	public int encode(ChannelBuffer buffer, int index) throws Exception {
		throw new RuntimeException("not implements");
	}
}