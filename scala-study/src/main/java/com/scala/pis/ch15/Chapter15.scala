package com.scala.pis.ch15

/**
 * @author cuckoo03
 */
object Chapter15 {
	def main(args: Array[String]) {
		val v = Var("x")
		println(v)

		val op = BinOp("+", Number(1), v)
		println(op)
		println(v.name)
		println(op.left)

		// pattern match
		simplifyTop(UnOp("-", UnOp("-", Var("x"))))

		println(describe(4))

		val capitals = Map("a" -> "1", "b" -> "2", "c" -> "3")
		println(capitals get "a")
		println(capitals.get("d"))
	}

	def simplifyTop(expr: Expr): Expr = expr match {
		case UnOp("-", UnOp("-", e)) => e
		case _ => expr
	}

	def describe(x: Any) = x match {
		case 5 => "five"
		case _ => "something"
	}
} 