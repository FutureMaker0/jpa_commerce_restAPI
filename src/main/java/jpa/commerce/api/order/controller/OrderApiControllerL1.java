package jpa.commerce.api.order.controller;

import jpa.commerce.api.order.dto.ObjectFormat;
import jpa.commerce.api.order.dto.OrderDtoL1;
import jpa.commerce.api.order.dto.OrderJpaDirectDto;
import jpa.commerce.api.order.repository.QueryRepository;
import jpa.commerce.domain.Order;
import jpa.commerce.domain.SearchOption;
import jpa.commerce.repository.OrderRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.stream.Collectors;

/**
 * collection이 아닌 __ToOne, join으로 인한 성능 저하가 비교적 덜 발생하는 저난이도의 API를 만든다.
 * order를 조회하면서 member, delivery 정보만 연관에 걸리도록 한다.
 */
@RestController
@RequiredArgsConstructor
public class OrderApiControllerL1 {

    private final OrderRepository orderRepository;
    private final QueryRepository queryRepository;

    @GetMapping("/api/v1/orders")
    public List<Order> ordersV1() {
        List<Order> allOrders = orderRepository.findAllOrders(new SearchOption());
        for (Order order : allOrders) {
            order.getMember().getName(); // LAZY loading 강제 초기화
            order.getDelivery().getAddress(); // LAZY loading 강제 초기화
        }
        return allOrders;
    }

    @GetMapping("/api/v2/orders")
    public List<OrderDtoL1> ordersV2() { // 실무에서는 List 반환은 안된다. Result로 감싸서 {} 객체 형태로 반환해야 한다.
        List<Order> allOrders = orderRepository.findAllOrders(new SearchOption());
        List<OrderDtoL1> resultDtoList = allOrders.stream()
                .map(o -> new OrderDtoL1(o))
                .collect(Collectors.toList());

        return resultDtoList;
    }

    @GetMapping("/api/v2-object/orders")
    public ObjectFormat objectOrdersV2() {
        List<Order> allOrders = orderRepository.findAllOrders(new SearchOption());
        List<OrderDtoL1> resultDtoList = allOrders.stream()
                .map(o -> new OrderDtoL1(o))
                .collect(Collectors.toList());

        return new ObjectFormat(resultDtoList);
    }

    @GetMapping("/api/v3/orders")
    public List<OrderDtoL1> ordersV3() {
        List<Order> allOrders = orderRepository.findAllUsingMemberDelivery();
        List<OrderDtoL1> resultDtoList = allOrders.stream()
                .map(order -> new OrderDtoL1(order))
                .collect(Collectors.toList());

        return resultDtoList;
    }

    @GetMapping("/api/v3-object/orders")
    public ObjectFormat objectOrdersV3() {
        List<Order> allOrders = orderRepository.findAllUsingMemberDelivery();
        List<OrderDtoL1> resultDtoList = allOrders.stream()
                .map(order -> new OrderDtoL1(order))
                .collect(Collectors.toList());

        return new ObjectFormat(resultDtoList);
    }

    @GetMapping("/api/v4/orders")
    public List<OrderJpaDirectDto> ordersV4() {
        return queryRepository.findOrderDtoList();
    }

    @GetMapping("/api/v4-object/orders")
    public ObjectFormat objectOrdersV4() {
        List<OrderJpaDirectDto> resultDtoList = queryRepository.findOrderDtoList();
        return new ObjectFormat(resultDtoList);
    }

}
