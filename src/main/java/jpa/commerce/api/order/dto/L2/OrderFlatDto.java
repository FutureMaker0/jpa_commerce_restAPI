package jpa.commerce.api.order.dto.L2;

import jpa.commerce.domain.Address;
import jpa.commerce.domain.OrderStatus;
import lombok.Data;

import java.time.LocalDateTime;

@Data
public class OrderFlatDto {
    // Order - OrderProduct - Product join 하여 한방 쿼리로 가져온다.

    private Long orderId;
    private String name;
    private LocalDateTime orderDate;
    private OrderStatus orderStatus;
    private Address address;

    private String productName;
    private int orderPrice;
    private int count;

    public OrderFlatDto(Long orderId,
                        String name,
                        LocalDateTime orderDate,
                        OrderStatus orderStatus,
                        Address address,
                        String productName,
                        int orderPrice,
                        int count) {
        this.orderId = orderId;
        this.name = name;
        this.orderDate = orderDate;
        this.orderStatus = orderStatus;
        this.address = address;
        this.productName = productName;
        this.orderPrice = orderPrice;
        this.count = count;
    }
}
