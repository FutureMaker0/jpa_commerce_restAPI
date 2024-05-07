package jpa.commerce.api.order.dto.L1;

import jpa.commerce.domain.Address;
import jpa.commerce.domain.Order;
import jpa.commerce.domain.OrderStatus;
import lombok.Data;

import java.time.LocalDateTime;

@Data
public class OrderJpaDirectDtoL1 {

    private Long orderId;
    private String name;
    private LocalDateTime orderDate;
    private OrderStatus orderStatus;
    private Address address;

    public OrderJpaDirectDtoL1(Long orderId, String name, LocalDateTime orderDate, OrderStatus orderStatus, Address address) {
        this.orderId = orderId;
        this.name = name;
        this.orderDate = orderDate;
        this.orderStatus = orderStatus;
        this.address = address;
    }

}
