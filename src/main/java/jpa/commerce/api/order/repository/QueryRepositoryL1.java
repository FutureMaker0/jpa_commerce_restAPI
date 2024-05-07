package jpa.commerce.api.order.repository;

import jakarta.persistence.EntityManager;
import jpa.commerce.api.order.dto.L1.OrderJpaDirectDtoL1;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
@RequiredArgsConstructor
public class QueryRepositoryL1 {

    private final EntityManager em;

    public List<OrderJpaDirectDtoL1> findOrderJpaDirectDtoL1List() {
        List<OrderJpaDirectDtoL1> resultDtoList = em.createQuery(
                "select new jpa.commerce.api.order.dto.OrderJpaDirectDto(o.id, m.name, o.orderDate, o.orderStatus, d.address)" +
                        " from Order o" +
                        " join o.member m" +
                        " join o.delivery d", OrderJpaDirectDtoL1.class
        ).getResultList();

        return resultDtoList;
    }
}
