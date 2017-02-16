package com.luceneinaction.ch01

import java.io.FileFilter
import java.io.File

/**
 * @author cuckoo03
 */
class TextFileFilter extends FileFilter {
	def accept(path: File): Boolean = {
		return path.getName.toLowerCase().endsWith(".txt")
	}
}