package com.scala.pis.ch25

/**
 * @author cuckoo03
 */
abstract class Base {
	case object A extends Base
	case object C extends Base
	case object G extends Base
	case object U extends Base

	object Base {
		val fromInt: Int => Base = Array(A, C, G, U)
		val toInt: Base => Int = collection.Map(A -> 0, C -> 1, G -> 2, U -> 3)
	}
}