package com.scala.pis.ch09

/**
 * @author cuckoo03
 */
object FileMatcher {
  private val filesHere = new java.io.File(".").listFiles()

  def filesEnding(query: String) = {
    for (
      file <- filesHere; if file.getName.endsWith(query)
    ) yield file
  }

  def files(matcher: (String) => Boolean) = {
    for (
      file <- filesHere; if matcher(file.getName)
    ) yield file
  }

  def filesEnding2(query: String) = {
    files(_.endsWith(query))
  }

  def main(args: Array[String]) {
    def f = FileMatcher.filesEnding2(".project")
    println(f.length)
  }
}