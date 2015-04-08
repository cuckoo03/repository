package com.scala.impatient.ch20

import scala.actors.Actor

object Ch20Main extends App {
	val actor1 = new HiActor()
	actor1.start()
	Thread.sleep(5000)
	actor1 ! "Hi"
	Thread.sleep(5000)
	actor1 ! "Hi"
}

class HiActor extends Actor {
	def act() {
		while (true) {
			receive {
				case "Hi" => println("Hello")
			}
		}
	}
}
