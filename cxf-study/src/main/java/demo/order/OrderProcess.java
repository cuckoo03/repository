package demo.order;

import javax.jws.WebService;

/**
 * 주문 처리 웹 서비스 SEI
 * @author x60
 *
 */
@WebService
public interface OrderProcess {
	String processOrder(Order order);
}
