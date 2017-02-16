package com.designpattern.lifegame

import org.junit.Test;

import com.holubonpatterns.lifegame.Publisher;

class TestCase {
	private static StringBuffer actualResults = new StringBuffer()
	private static StringBuffer expectedResults = new StringBuffer()
	
	interface observer {
		void notify(String arg)
	}
	
	static class Notifier {
		private Publisher publisher = new Publisher()	
	}

	@Test
	public void test() {
		
	}
}
