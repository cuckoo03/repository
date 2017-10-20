package com.designpattern.scala.ch04

import scala.collection.mutable.ListBuffer

/**
 * @author cuckoo03
 */
class IDCardFactory extends Factory {
  private val owners = ListBuffer[String]()
  override def createProduct(owner: String): Product = new IDCard(owner)
  override def registerProduct(product: Product): Unit =
    owners += (product.asInstanceOf[IDCard]).getOwner()
  def getOwners(): ListBuffer[String] = owners
}