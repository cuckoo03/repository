package com.gfpij.ch05

import groovy.transform.TypeChecked

@TypeChecked
class FileWriterExample {
	private final FileWriter writer
	FileWriterExample(String fileName) throws IOException {
		writer = new FileWriter(fileName)
	}
	def void write(String message) throws IOException {
		writer.write(message)
	}
	@Override
	def void finalize() throws IOException {
		writer.close()
	}
	def void close() throws IOException {
		writer.close()
	}
}
