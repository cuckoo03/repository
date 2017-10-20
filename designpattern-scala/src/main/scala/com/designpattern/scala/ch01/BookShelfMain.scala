package com.designpattern.scala.ch01

/**
 * @author cuckoo03
 */
object BookShelfMain {
  def main(args: Array[String]) = {
    var bookShelf = new BookShelf(3)
    bookShelf.appendBook(new Book("B"))
    bookShelf.appendBook(new Book("A"))
    val it = bookShelf.iterator()
    while (it.hasNext()) {
      val book = it.next()
      println(book.getName())
    }
  }
}