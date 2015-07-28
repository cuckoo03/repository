package com.scala.pis.ch12

/**
 * @author cuckoo03
 */
trait Doubling extends IntQueue {
	@Override
	abstract override def put(x: Int) {
		super.put(2 * x)
	}
}