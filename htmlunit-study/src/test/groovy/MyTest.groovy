import org.junit.Test

import groovy.transform.TypeChecked

@TypeChecked
class MyTest {
	@Test
	def void test() {
		println new Random().nextInt(3)
	}
}
