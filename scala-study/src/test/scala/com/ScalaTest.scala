package com

import org.junit.Test
import org.junit.runner.RunWith
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner
import org.springframework.test.context.ContextConfiguration
import org.springframework.beans.factory.annotation.Autowired
import com.beans.ScalaDao

/**
 * @author TC
 */
@RunWith(classOf[SpringJUnit4ClassRunner])
@ContextConfiguration(locations = Array("classpath:/spring/application-context.xml"))
class ScalaTest {

  @Test
  def test(): Unit = {
    println("test")
  }
}