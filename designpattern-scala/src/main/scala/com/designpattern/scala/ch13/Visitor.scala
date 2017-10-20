package com.designpattern.scala.ch13

/**
 * @author cuckoo03
 */
abstract class Visitor {
  def visit(file:File):Unit
  def visit(directory:Directory):Unit
}