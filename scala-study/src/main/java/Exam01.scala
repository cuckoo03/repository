import com.scala.pis.ch06.Rational

object Exam01 extends App {
	val oneHalf = new Rational(1, 2)
	val twoThrids = new Rational(2, 3)
	println(oneHalf add twoThrids)
	println(oneHalf + twoThrids)
	println("method overload " + oneHalf + 1)
	
	println(new Rational(3))

	println(new Rational(66, 42))
	
	
}