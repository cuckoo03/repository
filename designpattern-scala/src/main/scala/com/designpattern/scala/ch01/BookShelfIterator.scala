package com.designpattern.scala.ch01

/**
 * @author cuckoo03
 */
class BookShelfIterator(bookShelf: BookShelf) extends Iterator {
  private var index: Int = 0

  override def hasNext(): Boolean = {
    if (index < bookShelf.getLength()) {
      return true
    } else {
      return false
    }
  }

  override def next(): Book = {
    val book = bookShelf.getBookAt(index)
    index = index + 1
    return book
  }
}