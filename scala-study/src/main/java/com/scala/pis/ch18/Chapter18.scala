package com.scala.pis.ch17

/**
 * @author cuckoo03
 */
object Chapter18 {
	def main(args: Array[String]) {
		val account = new BankAccount
		println(account.deposit(100))
		println(account.withdraw(80))

		
	}
}