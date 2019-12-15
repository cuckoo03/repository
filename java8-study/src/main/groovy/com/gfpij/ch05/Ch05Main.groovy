package com.gfpij.ch05

import java.util.concurrent.locks.ReentrantLock

import groovy.transform.TypeChecked

@TypeChecked
class Ch05Main {
	static void main(args) {
		// write and close
		def writerExample = new FileWriterExample("f.txt")
//		writerExample.write("f")
//		writerExample.close()
		
		// write and autoclose
		new FileWriterARM("f.txt").withCloseable {
			it.write("f2")
			println "done with resource"
		}
		
		use("eam.txt", { writerEAM -> writerEAM.write("sweet") })
		use("eam2.txt", { writerEAM ->
			writerEAM.write("sweet1")
			writerEAM.write("\n")
			writerEAM.write("sweet2")
		})
		
		def lock = new ReentrantLock()
		Locker.runLocked(lock, { System.out.println("runLocked") })
		println lock.locked
	}
	static void use(String fileName, UseInstance<FileWriterEAM, IOException> block) {
		def writerEAM = new FileWriterEAM(fileName)
		try {
			block.accept(writerEAM)
		} finally {
			writerEAM.close()
		}
	}
}
