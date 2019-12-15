package com.gfpij.ch05

import groovy.transform.TypeChecked

@TypeChecked
class FileWriterARM implements AutoCloseable {
	private final FileWriter writer
	FileWriterARM(String fileName) throws IOException {
		writer = new FileWriter(fileName)
	}
	def void write(String message) throws IOException {
		writer.write(message)
	}
	@Override
	public void close() throws IOException {
		println "FileWriterARM close called automatically"
		writer.close()
	}
}
