package com.scala.pis.ch07

/**
 * @author cuckoo03
 */
object RefactoringMain {
  def main(args: Array[String]) {

    def forExam(row: Int) =
      for (col <- 1 to 5) yield {
        col + row
      }

    val r = forExam(10)
    println(s"result=$r")
    
    var f = (_:Int) + (_:Int)
    println(f)
    println(f(1,2))
  }
}