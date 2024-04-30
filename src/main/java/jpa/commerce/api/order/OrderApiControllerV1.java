package jpa.commerce.api.order;

import jpa.commerce.domain.Order;
import jpa.commerce.domain.SearchOption;
import jpa.commerce.repository.OrderRepository;
import jpa.commerce.service.OrderService;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * collection이 아닌 __ToOne, join으로 인한 성능 저하가 비교적 덜 발생하는 저난이도의 API를 만든다.
 * order를 조회하면서 member, delivery 정보만 연관에 걸리도록 한다.
 */
@RestController
@RequiredArgsConstructor
public class OrderApiControllerV1 {

    private final OrderRepository orderRepository;
    //private final OrderService orderService;

    @GetMapping("/api/v1/ordersV1")
    public List<Order> ordersV1() {
        List<Order> allOrders = orderRepository.findAllOrders(new SearchOption());
        return allOrders;
    }



}
