package com.errortest

/**
 * @author TC
 */
class Myclass1 {
  val myClass2 = new MyClass2()
  def method1():Unit = {
    val mapList = myClass2.getMapList()

    val mapList2 = myClass2.getMapList2()
  }
}