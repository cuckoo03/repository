package demo.order;

import org.springframework.context.support.ClassPathXmlApplicationContext;

public class Client {

	public static void main(String[] args) {
		ClassPathXmlApplicationContext context = new ClassPathXmlApplicationContext(
				new String[] { "spring/client-beans.xml" });

		OrderProcess client = (OrderProcess) context.getBean("client");
		Order order = new Order();

		String orderID = client.processOrder(order);
		System.out.println("Order ID: " + orderID);
		System.exit(0);
	}
}
