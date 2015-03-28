package com.apress.isf.spring.ch07;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import com.apress.isf.spring.ch06.MenuLoader;
/**
 * 어노테이션을 이용한 테스팅
 * @author cuckoo03
 *
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration("classpath:spring/application-context.xml")
public class PropertyTest {

	@Autowired
	private MenuLoader loader;

	@Test
	public void test() {
		loader.print("classpath:data/menu.txt");
	}
}
