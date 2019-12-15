package com.gfpij.ch05

import groovy.transform.TypeChecked

@TypeChecked
class FileWriterEAM {
	private FileWriter writer
	FileWriterEAM(String fileName) throws IOException {
		writer = new FileWriter(fileName)
	}
	def void close() throws IOException {
		println "fileWriterEAM close called automatically"
		writer.close()
	}
	def void write(String message) throws IOException {
		writer.write(message)
	}
}
