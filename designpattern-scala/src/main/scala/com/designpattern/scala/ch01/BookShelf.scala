package com.designpattern.scala.ch01

import scala.collection.mutable.ListBuffer
import scala.collection.mutable.ArrayBuffer

/**
 * @author cuckoo03
 */
class BookShelf(maxSize: Int) extends Aggregate {
  private val books = ArrayBuffer[Book]()
  private var last: Int = 0

  def getBookAt(index: Int): Book = {
    return books(index)
  }

  def appendBook(book: Book): Unit = {
    books += book
    last = last + 1
  }

  def getLength(): Int = {
    return last
  }

  override def iterator(): Iterator = {
    return new BookShelfIterator(this)
  }
}