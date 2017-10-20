package com.designpattern.scala.ch17

/**
 * @author cuckoo03
 */
object ObserverMain {
  def main(args: Array[String]) {
    val generator = new RandomNumberGenerator()
    val observer1 = new DigitObserver()
    val observer2 = new GraphObserver()
    generator.addObserver(observer1)
    generator.addObserver(observer2)
//    generator.execute()

    val generator2 = new IncrementalNumberGenerator(10, 50, 5)
    val observer3 = new DigitObserver()
    val observer4 = new GraphObserver()
    generator2.addObserver(observer3)
    generator2.addObserver(observer4)
    generator2.execute()
  }
}