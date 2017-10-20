package com.designpattern.scala.ch13

/**
 * @author cuckoo03
 */
object VisitorMain {
  def main(args: Array[String]) {
    val rootDir = new Directory("root")
    val binDir = new Directory("bin")
    val tmpDir = new Directory("tmp")
    rootDir.add(binDir)
    rootDir.add(tmpDir)
    binDir.add(new File("a", 1))
    binDir.add(new File("b", 2))
    rootDir.accept(new ListVisitor())
  }
}