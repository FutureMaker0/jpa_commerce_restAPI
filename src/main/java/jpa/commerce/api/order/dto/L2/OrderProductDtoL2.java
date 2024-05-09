package jpa.commerce.api.order.dto.L2;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Data;

@Data
public class OrderProductDtoL2 {

    @JsonIgnore
    private Long orderId;
    private String productName;
    private int orderPrice;
    private int count;

    public OrderProductDtoL2(Long orderId, String ProductName, int orderPrice, int count) {
        this.orderId = orderId;
        this.productName = productName;
        this.orderPrice = orderPrice;
        this.count = count;
    }

}
