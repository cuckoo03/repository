package com.scala.pis.ch32

import scala.actors.Actor
import java.net.InetAddress
import java.net.UnknownHostException
/**
 * @author cuckoo03
 */
object Chapter32 {
	def main(args: Array[String]) {
//		makeActor()
		//		makeActor2()
		//				nativethread()
		//				react()
		//		react2()
		//		emote()
	}

	def nativethread() = {
		Actor.self ! "hello"
		Actor.self.receive { case x => println(x) }
	}

	def makeActor2() = {
		val intActor = Actor.actor {
			Actor.receive {
				case x: Int =>
					println("Int:" + x)
				case msg =>
					println("msg:" + msg)
			}
		}

		intActor ! "hello"
		intActor ! 1
		Thread.sleep(2000)
		println("waiting")
	}

	def makeActor() = {
		//		SillyActor.start()

		val actor2 = Actor.actor {
			for (i <- 1 to 5) {
				println("actor2")
				Thread.sleep(1000)
			}
		}

		val echoActor = Actor.actor {
			while (true) {
				 def a = Actor.receive {
					case msg =>
						println("message:" + msg)
				}
			}
		}
		echoActor ! "hi message"
	}

	def emote() = {
		val sillyActor2 = Actor.actor {

			def emoteLater() {
				val mainActor = Actor.self
				Actor.actor {
					Thread.sleep(1000)
					mainActor ! "Emote"
				}
			}

			var emoted = 0
			emoteLater()

			Actor.loop {
				Actor.react {
					case "Emote" =>
						println("im acting")
						emoted += 1
						if (emoted < 5)
							emoteLater()
					case msg =>
						println("received:" + msg)
				}
			}
		}
		sillyActor2 ! "hi"
	}

	def react2() = {
		var reactActor = Actor.actor {
			Actor.react {
				case x: String =>
					println(x)
			}
		}

		Thread.sleep(3000)
		reactActor ! "hello"
	}

	def react() = {
		NameResolver.start()
		NameResolver ! ("www.scala-lang.org", Actor.self)
		Actor.self.receiveWithin(0) { case x => x }
		NameResolver ! ("wwwww.scala-lang.org", Actor.self)
		Actor.self.receiveWithin(0) { case x => x }
	}

	object NameResolver extends Actor {
		def act() {
			react {
				case (name: String, actor: Actor) =>
					actor ! getIp(name)
					act()
				case "EXIT" =>
					println("exsiting")
				case msg =>
					println("msg:" + msg)
					act()
			}
		}

		def getIp(name: String): Option[InetAddress] = {
			try {
				Some(InetAddress.getByName(name))
			} catch {
				case _: UnknownHostException => None
			}
		}
	}

	object SillyActor extends Actor {
		def act() {
			for (i <- 1 to 5) {
				println("acting")
				Thread.sleep(1000)
			}
		}
	}
}