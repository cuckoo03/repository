package com.gfpij.ch05

import java.util.concurrent.locks.Lock
import java.util.concurrent.locks.ReentrantLock

import groovy.transform.TypeChecked

@TypeChecked
class Locker {
	def static void runLocked(Lock lock, Runnable block) {
		lock.lock()
		try {
			block.run()
		} finally {
			lock.unlock()
		}
	}
	
}
