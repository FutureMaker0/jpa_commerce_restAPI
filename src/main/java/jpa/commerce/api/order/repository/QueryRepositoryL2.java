package jpa.commerce.api.order.repository;

import jakarta.persistence.EntityManager;
import jpa.commerce.api.order.dto.L2.OrderFlatDto;
import jpa.commerce.api.order.dto.L2.OrderJpaDirectDtoL2;
import jpa.commerce.api.order.dto.L2.OrderProductDtoL2;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

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

    //=============================================================
    public List<OrderJpaDirectDtoL2> findOrderJpaDirectDtoL2List_optimized() {
        // root 1번 조회
        List<OrderJpaDirectDtoL2> resultOrders = findOrderList();

        // Order 데이터 만큼 메모리에 올린다.
        List<Long> orderIds = getOrderIds(resultOrders);
        Map<Long, List<OrderProductDtoL2>> orderProductMap = getOrderProductMap(orderIds);

        // 루프 돌며 컬렉션 데이터를, 메모리에 올려놓은 데이터를 활용하여 채운다.
        resultOrders.forEach(o -> o.setOrderProductList(orderProductMap.get(o.getOrderId())));

        // 결과값 반환
        return resultOrders;
    }

    private Map<Long, List<OrderProductDtoL2>> getOrderProductMap(List<Long> orderIds) {
        List<OrderProductDtoL2> orderProductList = em.createQuery(
                        "select new jpa.commerce.api.order.dto.L2.OrderProductDtoL2(op.order.id, p.name, op.orderPrice, op.count)" +
                                " from OrderProduct op" +
                                " join op.product p" +
                                " where op.order.id in :orderIds", OrderProductDtoL2.class)
                .setParameter("orderIds", orderIds)
                .getResultList();

        Map<Long, List<OrderProductDtoL2>> orderProductMap = orderProductList.stream()
                .collect(Collectors.groupingBy(OrderProductDtoL2 -> OrderProductDtoL2.getOrderId()));

        return orderProductMap;
    }

    private static List<Long> getOrderIds(List<OrderJpaDirectDtoL2> resultOrders) {
        List<Long> orderIds = resultOrders.stream()
                .map(o -> o.getOrderId())
                .collect(Collectors.toList());

        return orderIds;
    }
    //=============================================================

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

    public List<OrderFlatDto> findOrderJpaDirectDtoL2List_flatData() {
        List<OrderFlatDto> flatDtoList = em.createQuery(
                        "select new jpa.commerce.api.order.dto.L2.OrderFlatDto(o.id, m.name, o.orderDate, o.orderStatus, d.address, p.name, op.orderPrice, op.count)" +
                                " from Order o" +
                                " join o.member m" +
                                " join o.delivery d" +
                                " join o.orderProducts op" +
                                " join op.product p", OrderFlatDto.class)
                .getResultList();

        return flatDtoList;
    }

}
