package com.scala.pis.ch12

/**
 * @author cuckoo03
 */
trait Incrementing extends IntQueue {
	@Override
	abstract override def put(x: Int) {
		super.put(x + 1)
	}
}
trait Filtering extends IntQueue {
	@Override
	abstract override def put(x:Int) {
		if (x >= 0)
			super.put(x)
	}
}