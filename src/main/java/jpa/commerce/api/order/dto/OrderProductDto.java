package jpa.commerce.api.order.dto;

import jpa.commerce.domain.OrderProduct;
import lombok.Data;

@Data
public class OrderProductDto {

    private String name;
    private int orderPrice;
    private int count;

    public OrderProductDto(OrderProduct orderProduct) {
        name = orderProduct.getProduct().getName();
        orderPrice = orderProduct.getOrderPrice();
        count = orderProduct.getCount();
    }
}
