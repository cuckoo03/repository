package com.scala.pis.ch12

/**
 * @author cuckoo03
 */
object Chap12 {
	def main(args: Array[String]) {
		val frog = new Frog()
		println(frog)
		frog.philosophize()
		println("----------")
		
		val phil:Philosophical = new Frog()
		phil.philosophize()
		println("----------")
		
		val queue = new BasicIntQueue()
		queue.put(10)
		queue.put(20)
		println(queue.get())
		println(queue.get())
		println("----------")

		val queue2 = new MyQueue()
		queue2.put(10)
		println(queue2.get())
		println("----------")
		
		
		val queue3 = new BasicIntQueue with Incrementing with Filtering
		queue3.put(-1)
		queue3.put(0)
		queue3.put(1)
		println(queue3.get())
		println(queue3.get())
	}
}