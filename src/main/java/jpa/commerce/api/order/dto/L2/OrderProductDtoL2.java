package jpa.commerce.api.order.dto.L2;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Data;

@Data
public class OrderProductDtoL2 {

    @JsonIgnore
    private Long orderId;
    private String name;
    private int orderPrice;
    private int count;

    public OrderProductDtoL2(Long orderId, String name, int orderPrice, int count) {
        this.orderId = orderId;
        this.name = name;
        this.orderPrice = orderPrice;
        this.count = count;
    }

}
