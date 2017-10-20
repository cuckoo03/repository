package com.designpattern.scala.ch15

import java.io.Writer

/**
 * @author cuckoo03
 */
class HtmlWriter(_writer: Writer) {
  private val writer = _writer

  def title(title: String): Unit = {
    writer.write("<html><body>")
    writer.write(s"<title>$title</title>")
    writer.write(s"<body>$title</body>")
  }
  def close(): Unit = {
    writer.write("</body></html>")
    writer.close()
  }
}