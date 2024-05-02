package jpa.commerce.api.order.dto;

import jpa.commerce.domain.Address;
import jpa.commerce.domain.Order;
import jpa.commerce.domain.OrderStatus;
import lombok.Data;

import java.time.LocalDateTime;

@Data
public class OrderJpaDirectDto {

    private Long orderId;
    private String name;
    private LocalDateTime orderDate;
    private OrderStatus orderStatus;
    private Address address;

    public OrderJpaDirectDto(Long orderId, String name, LocalDateTime orderDate, OrderStatus orderStatus, Address address) {
        this.orderId = orderId;
        this.name = name;
        this.orderDate = orderDate;
        this.orderStatus = orderStatus;
        this.address = address;
    }

}
