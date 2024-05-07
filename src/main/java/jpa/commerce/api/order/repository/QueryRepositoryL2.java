package jpa.commerce.api.order.repository;

import jakarta.persistence.EntityManager;
import jpa.commerce.api.order.dto.L2.OrderJpaDirectDtoL2;
import jpa.commerce.api.order.dto.L2.OrderProductDtoL2;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
@RequiredArgsConstructor
public class QueryRepositoryL2 {

    private final EntityManager em;

    public List<OrderJpaDirectDtoL2> findOrderJpaDirectDtoL2List() {
        List<OrderJpaDirectDtoL2> resultDtoList = findOrderList();

        // 컬렉션 부분은 위에서 처리가 안되므로, 다시 직접 가져와서 루프 돌면서 초기화(컬렉션 채우는 것) 작업을 추가적으로 해주고 있다.
        resultDtoList.forEach(o -> {
            List<OrderProductDtoL2> orderProductList = findOrderProductList(o.getOrderId());
            o.setOrderProductList(orderProductList);
        });
        return resultDtoList;
    }

    private List<OrderProductDtoL2> findOrderProductList(Long orderId) {
        List<OrderProductDtoL2> resultDtoList = em.createQuery(
                        "select new jpa.commerce.api.order.dto.L2.OrderProductDtoL2(op.order.id, p.name, op.orderPrice, op.count)" +
                                " from OrderProduct op" +
                                " join op.product p" +
                                " where op.order.id = :orderId", OrderProductDtoL2.class)
                .setParameter("orderId", orderId)
                .getResultList();

        return resultDtoList;
    }

    private List<OrderJpaDirectDtoL2> findOrderList() {
        List<OrderJpaDirectDtoL2> resultDtoList = em.createQuery(

                // OrderPruductList는 new operation에서 파라미터로 넘겨줄 수 없다.
                // 생성자에서 채워주지 못한 orderProductList의 값을 이후 어디선가 채워줘야 한다.
                "select new jpa.commerce.api.order.dto.L2.OrderJpaDirectDtoL2(o.id, m.name, o.orderDate, o.orderStatus, d.address)" +
                        " from Order o" +
                        " join o.member m" +
                        " join o.delivery d", OrderJpaDirectDtoL2.class
        ).getResultList();

        return resultDtoList;
    }


}
