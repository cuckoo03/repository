package com.designpattern.scala.ch04

/**
 * @author cuckoo03
 */
abstract class Factory {
  final def create(owner: String): Product = {
    val p = createProduct(owner)
    return p
  }
  protected def createProduct(owner: String): Product =
    throw new Exception("must override createProduct method.")
  protected def registerProduct(product: Product): Unit
}