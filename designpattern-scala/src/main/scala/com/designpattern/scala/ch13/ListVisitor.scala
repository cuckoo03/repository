package com.designpattern.scala.ch13

/**
 * @author cuckoo03
 */
class ListVisitor extends Visitor {
  private var currentDir = ""

  def visit(file: File): Unit = {
    println(s"$currentDir/$file")
  }

  def visit(directory: Directory): Unit = {
    println(s"$currentDir/$directory")
    val saveDir = currentDir
    currentDir = currentDir + "/" + directory.getName()
    val it = directory.iterator()
    while (it.hasNext) {
      val entry = it.next()
      entry.accept(this)
    }
    currentDir = saveDir
  }
}