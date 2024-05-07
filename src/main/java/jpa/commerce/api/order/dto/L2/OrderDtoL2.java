package jpa.commerce.api.order.dto.L2;

import jpa.commerce.api.order.dto.L1.OrderProductDtoL1;
import jpa.commerce.domain.Address;
import jpa.commerce.domain.Order;
import jpa.commerce.domain.OrderStatus;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Data
public class OrderDtoL2 {

    private Long orderId;
    private String name;
    private LocalDateTime orderDate;
    private OrderStatus orderStatus;
    private Address address;
    private List<OrderProductDtoL1> orderProductList;

    public OrderDtoL2(Order order) {
        orderId = order.getId();
        name = order.getMember().getName();
        orderDate = order.getOrderDate();
        orderStatus = order.getOrderStatus();
        address = order.getDelivery().getAddress();

        orderProductList = order.getOrderProducts().stream()
                .map(orderProduct -> new OrderProductDtoL1(orderProduct))
                .collect(Collectors.toList());
    }

}
