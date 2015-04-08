package com.hadoop.beginnerhadoop.yarn.helloyarn

import groovy.transform.TypeChecked;

import java.util.stream.Nodes.ToArrayTask;

@TypeChecked
class HelloYarn {
	private static final long MEGABYTE = 1024L * 1024L
	public HelloYarn1() {
		println "helloyarn"
	}

	public static long bytesToMegabytes(long bytes) {
		return (long)(bytes / MEGABYTE)
	}

	public void printMemoryStats() {
		long freeMemory = bytesToMegabytes(Runtime.getRuntime().freeMemory())
		long totalMemory = bytesToMegabytes(Runtime.getRuntime().totalMemory())
		long maxMemory = bytesToMegabytes(Runtime.getRuntime().maxMemory())
		println "freeMemory:$freeMemory"
		println "totalMemory:$totalMemory"
		println "maxMemory:$maxMemory"
	}

	static main(args) {
		HelloYarn yarn = new HelloYarn()
		yarn.printMemoryStats()
	}
}