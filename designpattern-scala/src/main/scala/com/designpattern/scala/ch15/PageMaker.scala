package com.designpattern.scala.ch15

import java.io.IOException
import java.io.FileWriter

/**
 * @author cuckoo03
 */
class PageMaker {

}
object PageMaker {
  def makeWelcomePage(fileName: String): Unit = {
    try {
      val prop = Database.getProperties("maildata")
      val value1 = prop.getProperty("key1")
      val writer = new HtmlWriter(new FileWriter(fileName))
      writer.title(value1)
      writer.close()
      println(s"$fileName is create for $value1")
    } catch {
      case e: IOException => {
        e.printStackTrace()
      }
    }
  }
}