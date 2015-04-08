package com.scala.impatient.ch10

object Ch10Main extends App {
	val logger:Logger = new ConsoleLogger
	logger.log("log")
}
trait Logger {
	def log(msg: String) {
		println("super" + msg)
	}
}
class ConsoleLogger extends Logger with Cloneable {
	override def log(msg: String) {
		println("concret" +msg)
	}
}
