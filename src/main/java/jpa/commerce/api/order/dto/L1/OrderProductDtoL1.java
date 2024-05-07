package jpa.commerce.api.order.dto.L1;

import jpa.commerce.domain.OrderProduct;
import lombok.Data;

@Data
public class OrderProductDtoL1 {

    private String name;
    private int orderPrice;
    private int count;

    public OrderProductDtoL1(OrderProduct orderProduct) {
        name = orderProduct.getProduct().getName();
        orderPrice = orderProduct.getOrderPrice();
        count = orderProduct.getCount();
    }
}
