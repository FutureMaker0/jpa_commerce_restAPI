package jpa.commerce.api.order.controller;

import jpa.commerce.api.member.dto.CustomFormat;
import jpa.commerce.api.order.dto.OrderDtoL2;
import jpa.commerce.domain.Order;
import jpa.commerce.domain.OrderProduct;
import jpa.commerce.domain.SearchOption;
import jpa.commerce.repository.OrderRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequiredArgsConstructor
public class OrderApiControllerL2 {

    private final OrderRepository orderRepository;

    @GetMapping("/api/v1-collection/orders")
    public List<Order> orderListV1() {
        List<Order> allOrders = orderRepository.findAllOrders(new SearchOption());
        for (Order order : allOrders) {
            order.getMember().getName();
            order.getDelivery().getAddress();
            List<OrderProduct> orderProducts = order.getOrderProducts();
            for (OrderProduct orderProduct : orderProducts) {
                orderProduct.getProduct().getName();
            }
        }
        return allOrders;
    }

    @GetMapping("/api/v1-collection-object/orders")
    public CustomFormat objectOrderListV1() {
        List<Order> allOrders = orderRepository.findAllOrders(new SearchOption());
        for (Order order : allOrders) {
            order.getMember().getName();
            order.getDelivery().getAddress();
            List<OrderProduct> orderProducts = order.getOrderProducts();
            for (OrderProduct orderProduct : orderProducts) {
                orderProduct.getProduct().getName();
            }
        }
        return new CustomFormat(allOrders);
    }

    @GetMapping("/api/v2-collection/orders")
    public List<OrderDtoL2> orderListV2() {
        List<Order> allOrders = orderRepository.findAllOrders(new SearchOption());
        // Order엔티티를 OrderDto로 변환. 만만치 않은 과정.
        List<OrderDtoL2> resultDtoList = allOrders.stream()
                .map(order -> new OrderDtoL2(order))
                .collect(Collectors.toList());

        return resultDtoList;
    }

    @GetMapping("/api/v2-collection-object/orders")
    public CustomFormat objectOrderListV2() {
        List<Order> allOrders = orderRepository.findAllOrders(new SearchOption());
        // Order엔티티를 OrderDto로 변환. 만만치 않은 과정.
        List<OrderDtoL2> resultDtoList = allOrders.stream()
                .map(order -> new OrderDtoL2(order))
                .collect(Collectors.toList());

        return new CustomFormat(resultDtoList);
    }

    @GetMapping("/api/v3-collection/orders")
    public List<OrderDtoL2> orderListV3() {
        List<Order> allOrders = orderRepository.findAllUsingProduct();
        List<OrderDtoL2> resultDtoList = allOrders.stream()
                .map(order -> new OrderDtoL2(order))
                .collect(Collectors.toList());

        return resultDtoList;
    }

    @GetMapping("/api/v3-collection-object/orders")
    public CustomFormat objectOrderListV3() {
        List<Order> allOrders = orderRepository.findAllUsingProduct();
        List<OrderDtoL2> resultDtoList = allOrders.stream()
                .map(order -> new OrderDtoL2(order))
                .collect(Collectors.toList());

        return new CustomFormat(resultDtoList);
    }

    @GetMapping("/api/v3.1-collection/orders")
    public List<OrderDtoL2> orderListV3_1(
            @RequestParam(value = "offset", defaultValue = "0") int offset,
            @RequestParam(value = "limit", defaultValue = "10") int limit) {
        List<Order> allOrders = orderRepository.findAllUsingMemberDelivery(offset, limit);
        List<OrderDtoL2> resultDtoList = allOrders.stream()
                .map(order -> new OrderDtoL2(order))
                .collect(Collectors.toList());

        return resultDtoList;
    }

    @GetMapping("/api/v3.1-collection-object/orders")
    public CustomFormat orderOrderListV3_1(
            @RequestParam(value = "offset", defaultValue = "0") int offset,
            @RequestParam(value = "limit", defaultValue = "10") int limit) {
        List<Order> allOrders = orderRepository.findAllUsingMemberDelivery(offset, limit);
        List<OrderDtoL2> resultDtoList = allOrders.stream()
                .map(order -> new OrderDtoL2(order))
                .collect(Collectors.toList());

        return new CustomFormat(resultDtoList);
    }




}
