package com.apress.isf.spring.ch06;

import org.junit.Test;
import org.springframework.context.support.ClassPathXmlApplicationContext;

/**
 * bean에서 리소스 불러오기
 * 
 * @author cuckoo03
 *
 */
public class ResourceBeanTest {
	@Test
	public void test() {
		ClassPathXmlApplicationContext context = new ClassPathXmlApplicationContext(
				"spring/application-context.xml");
		Menu m = context.getBean(Menu.class);
		m.print();

		MenuLoader ml = context.getBean(MenuLoader.class);
		ml.print("classpath:data/menu.txt");
	}
}
