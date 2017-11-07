package com.groovy.release.r1_6

import groovy.transform.TypeChecked

import java.text.SimpleDateFormat
import java.util.concurrent.locks.Lock
import java.util.concurrent.locks.ReentrantLock

import javax.management.InstanceOfQueryExp;

@TypeChecked
class DelegateExam {
	static main(args) {
		def df = new SimpleDateFormat("yyyy/MM/dd")
		def e1 = new Event(title:"title1", url:"url1", when:df.parse("2009/05/18"))
		def e2 = new Event(title:"title2", url:"url2", when:df.parse("2009/06/02"))
		assert e1.before(e2.when)
		
		def list = new LockableList()
		list.lock()
		try {
			list << "a"
			list << "b"
			list << "c"
		} finally {
			list.unlock()
		}
		assert list.size() == 3
	}
}
class Event {
	@Delegate
	Date when
	String title, url
}
class LockableList {
	@Delegate private List<String> list = []
	@Delegate private Lock loc = new ReentrantLock()
}