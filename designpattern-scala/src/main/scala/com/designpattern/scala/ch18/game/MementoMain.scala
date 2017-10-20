package com.designpattern.scala.ch18.game

/**
 * @author cuckoo03
 */
object MementoMain {
  def main(args: Array[String]) {
    val gamer = new Gamer(100)
    var memento = gamer.createMemento()
    for (i<- 0 until 100) {
    	println("")
      println(s"---$i")
      println(s"current:$gamer")
      
      gamer.bet()
      
      println(s"money:" + gamer.getMoney())
      
      if (gamer.getMoney() > memento.getMoney()) {
        println("save money")
        memento = gamer.createMemento()
      } else if (gamer.getMoney() < memento.getMoney() / 2) {
        println("restore money")
        gamer.restoreMemento(memento)
      } 
      
      Thread.sleep(500)
    }
  }
}