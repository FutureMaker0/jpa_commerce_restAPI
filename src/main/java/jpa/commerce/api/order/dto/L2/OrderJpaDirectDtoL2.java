package jpa.commerce.api.order.dto.L2;

import jpa.commerce.domain.Address;
import jpa.commerce.domain.OrderStatus;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.List;

@Data
public class OrderJpaDirectDtoL2 {

    private Long orderId;
    private String name;
    private LocalDateTime orderDate;
    private OrderStatus orderStatus;
    private Address address;
    private List<OrderProductDtoL2> orderProductList;

    public OrderJpaDirectDtoL2(Long orderId,
                               String name,
                               LocalDateTime orderDate,
                               OrderStatus orderStatus,
                               Address address) {
        this.orderId = orderId;
        this.name = name;
        this.orderDate = orderDate;
        this.orderStatus = orderStatus;
        this.address = address;
    }

}
