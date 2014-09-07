package com.scala.impatient.ch02

object Ch02Main extends App {
  val x = -1
  val s = if (x > 0) 1 else -1
  assert (-1 == s)
  
  var s1 = if (x > 0) 1 else ()
  assert (() == s1)
  
  printf ("hello, %s, %d \n", "World", 123);
}