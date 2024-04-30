package jpa.commerce.domain;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import jpa.commerce.domain.product.Product;
import lombok.Getter;
import lombok.Setter;

@Entity
@Getter
@Setter
@Table(name = "order_product")
public class OrderProduct {

    @Id
    @GeneratedValue
    @Column(name = "order_product_id")
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "product_id")
    private Product product;

    @JsonIgnore
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "order_id")
    private Order order;

    private int orderPrice;
    private int count;


    //== 생성 메서드 ==//
    public static OrderProduct createOrderProduct(Product product, int orderPrice, int count) {
        OrderProduct orderProduct = new OrderProduct();
        orderProduct.setProduct(product);
        orderProduct.setOrderPrice(orderPrice);
        orderProduct.setCount(count);
        product.minusStock(count);

        return orderProduct;
    }


    //== 비즈니스 로직 ==//
    /**
     * 주문상품 취소
     * 가지고 있는 주문 수량을 이용해서 취소 후 수량원복을 위한 로직 실행
     */
    public void orderItemCancel() {
        product.plusStock(count);
        //getProduct().plusStock(count);
    }

    /**
     * 전체 주문상품 가격 조회
     * 실제 정수 총금액 값을 구하는 산술 로직
     */
    public int getTotalPrice() {
        int totalPrice = 0;
        totalPrice += getOrderPrice() * getCount();
        return totalPrice;
    }
}
