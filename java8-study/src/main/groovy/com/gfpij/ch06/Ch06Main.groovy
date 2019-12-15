package com.gfpij.ch06

import groovy.transform.TypeChecked
import java.util.function.Supplier

@TypeChecked
class Ch06Main {
	static void main(args) {
		def holder = new HolderNaive()
		println "deffering heavy creation"
		println holder.getHeavy()
		println holder.getHeavy()
		
		def supplier = { -> new Heavy() } as Supplier<Heavy>
		def supplier2 = Heavy.&new as Heavy
		
	}
}
