package jpa.commerce.api.order.dto.L1;

import jpa.commerce.domain.Address;
import jpa.commerce.domain.Order;
import jpa.commerce.domain.OrderStatus;
import lombok.Data;

import java.time.LocalDateTime;

@Data
public class OrderDtoL1 {
    private Long orderId;
    private String name;
    private LocalDateTime orderDate;
    private OrderStatus orderStatus;
    private Address address;

    public OrderDtoL1(Order order) {
        orderId = order.getId();
        name = order.getMember().getName();
        orderDate = order.getOrderDate();
        orderStatus = order.getOrderStatus();
        address = order.getDelivery().getAddress();
    }

}
