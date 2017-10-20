package com.designpattern.scala.ch12

/**
 * @author cuckoo03
 */
object DecoratorMain {
  def main(args: Array[String]) {
    val b1 = new StringDisplay("Hello")
    val b2 = new SideBorder(b1, '#')
    val b3 = new FullBorder(b2)
    val b4 = new FullBorder(new UpDownBorder(new SideBorder(
      new UpDownBorder(new SideBorder(
        new StringDisplay("Hello"), '*'), '='), '|'), '/'))

    b1.show()
    println("")

    b2.show()
    println("")

    b3.show()
    println("")

    b4.show()
    
    val md = new MultiStringDisplay()
    md.add("ab")
    md.add("cde")
    md.add("f")
    md.show()
    
    val d1 = new SideBorder(md, '#')
    d1.show()
    
    val d2 = new FullBorder(md)
    d2.show()
  }
}