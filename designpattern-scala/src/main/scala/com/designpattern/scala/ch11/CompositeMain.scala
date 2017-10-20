package com.designpattern.scala.ch11

/**
 * @author cuckoo03
 */
object CompositeMain {
  def main(args: Array[String]) {
    val rootDir = new Directory("root")
    val binDir = new Directory("bin")
    val cinDir = new Directory("cin")
    rootDir.add(binDir)
    rootDir.add(cinDir)
    binDir.add(new File("a", 1))
    binDir.add(new File("b", 2))
    rootDir.printList()
  }
}