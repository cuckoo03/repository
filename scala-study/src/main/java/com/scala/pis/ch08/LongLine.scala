package com.scala.pis.ch08

import scala.io.Source

/**
 * @author cuckoo03
 */
object LongLine {
	def processFile(filename: String, width: Int) {

		def processLine(line: String) = {
			if (line.length > width)
				println(filename + ":" + line.trim())
		}

		val source = Source.fromFile(filename)
		for (line <- source.getLines())
			processLine(line)
	}
}