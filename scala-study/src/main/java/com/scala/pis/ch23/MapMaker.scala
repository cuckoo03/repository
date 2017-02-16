package com.scala.pis.ch23

import scala.collection.mutable.{ Map, HashMap, SynchronizedMap }

/**
 * @author cuckoo03
 */
object MapMaker {
	def makeMap: Map[String, String] = {
		new HashMap[String, String] with SynchronizedMap[String, String] {
			@Override
			override def default(key: String) = "why"
		}
	}
}