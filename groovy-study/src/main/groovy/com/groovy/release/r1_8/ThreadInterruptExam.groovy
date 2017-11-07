package com.groovy.release.r1_8

import groovy.transform.TimedInterrupt
import groovy.transform.TypeChecked

import java.util.concurrent.TimeUnit

//@ThreadInterrupt
@TimedInterrupt(1L)
@TypeChecked
class ThreadInterruptExam {
	static main(args) {
		while (true) {
			if (Thread.currentThread().isInterrupted())
				throw new InterruptedException("execution interrupted")
		}
	}
}
