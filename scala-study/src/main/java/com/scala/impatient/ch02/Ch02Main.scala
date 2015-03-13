package com.scala.impatient.ch02

object Ch02Main extends App {
  val x = -1
  val s = if (x > 0) 1 else -1
  assert(-1 == s)

  var s1 = if (x > 0) 1 else ()
  assert(() == s1)

  printf("hello, %s, %d \n", "World", 123)

  val distance = { val dx = 1; val dy = 2; Math.ceil(dx + dy) }
  println("distance:" + distance)

  // input and output
  //  val name = readLine("your name:")
  //  print("age:")
  //  val age = readInt()
  //  printf("%s, %d", name, age)

  //loop
  var a = 0;
  var b = 0;
  // for loop execution with a range
  for (a <- 1 to 5) {
    b = b * a;
    println("Value of a: " + a);
  }
  println(b);

  var r = 0;
  var n = 3;
  while (n > 0) {
    r = r + n;
    n -= 1;
  }
  println(r);

  // functions
  def abs(x: Double): Double = if (x >= 0) x else -x;
  println(abs(1));

  def abs2(x: Double): Int = {
    if (x >= 0) 1 else 2;
    3;
  }
  println(abs2(1));

  def decorate(str: String, left: String = "[", right: String = "]") = left + str + right;
  println(decorate("Hello"));
  println(decorate("Hello", right = "<<"));

  def proc(s: String): Unit = {
    var l = s;
  }
  println(proc("a"));
}