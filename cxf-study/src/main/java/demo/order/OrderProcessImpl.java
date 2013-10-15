package demo.order;

import javax.jws.WebService;

/**
 * OrderProcess 서비스 구현
 * @author x60
 *
 */
@WebService(endpointInterface = "demo.order.OrderProcess")
public class OrderProcessImpl implements OrderProcess {
	public String processOrder(Order order) {
		return order.validate();
	}
}
