package com.designpattern.scala.ch06

/**
 * @author cuckoo03
 */
class Manager {
  private val showcase = scala.collection.mutable.Map[String, Product]()
  def register(name: String, product: Product): Unit = {
    showcase(name) = product
  }

  def create(prototypeName: String): Product = {
    def p = showcase.get(prototypeName)
    return p.get.createClone()
  }
}