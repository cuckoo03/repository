package com.agile.ch24

import groovy.transform.TypeChecked

import org.junit.Assert
import org.junit.Test

@TypeChecked
class ClockDriverTest {
	
	@Test
	public void testTimeChange() {
		MockTimeSource source = new MockTimeSource()
		MockTimeSink sink = new MockTimeSink()
		source.registerObserver(sink)
		
		source.setTime(3, 4, 5)
		Assert.assertEquals(3, sink.getHours())
		
		source.setTime(7, 8, 9)
		Assert.assertEquals(7, sink.getHours())
	}
}
