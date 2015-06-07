import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.GenericXmlApplicationContext;

import com.mybatis.service.IMybatisService;
import com.mybatis.service.MybatisService;

public class DaoTest {
	@Autowired
	private IMybatisService mybatisService;

	public void start() {
		ApplicationContext context = new GenericXmlApplicationContext(
				"classpath:local/spring/application-context.xml");

		mybatisService = context.getBean(MybatisService.class);
		mybatisService.insert();
		mybatisService.select();
		mybatisService.delete();
		mybatisService.select();

	}

	public static void main(String[] args) {
		DaoTest test = new DaoTest();
		test.start();
	}
}
