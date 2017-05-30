import org.springframework.test.context.junit4.SpringJUnit4ClassRunner
import org.junit.runner.RunWith
import org.springframework.test.context.ContextConfiguration
import org.junit.Test
/**
 * @author TC
 */
@RunWith(classOf[SpringJUnit4ClassRunner])
@ContextConfiguration(locations = Array("classpath:spring/application-context.xml"))
class SpringTest {
  @Test
  def test(): Unit = {
  }
}