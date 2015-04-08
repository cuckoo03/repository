package com.scala.impatient.ch07

import scala.collection.mutable.ArrayBuffer
import scala.collection.mutable.HashMap

object Ch07Main extends App {
	val a = new org.com.A()
	a.a()
	
	val hashMap = new HashMap() 
}
package org {
	package com {
		class A {
			def a() {
				println("a")
			}
		}
	}
}
package com {
	package hostman {
		package impatient {
			class Manager {
				val sub = new collection.mutable.ArrayBuffer()
			}
		}
	}
}
package com {
	package hostman {
		package collections {
			
		}
	}
}