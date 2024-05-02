package jpa.commerce.api.order.repository;

import jakarta.persistence.EntityManager;
import jpa.commerce.api.order.dto.OrderJpaDirectDto;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import javax.swing.text.html.parser.Entity;
import java.util.List;

@Repository
@RequiredArgsConstructor
public class QueryRepository {

    private final EntityManager em;

    public List<OrderJpaDirectDto> findOrderDtoList() {
        List<OrderJpaDirectDto> resultDtoList = em.createQuery(
                "select new jpa.commerce.api.order.dto.OrderJpaDirectDto(o.id, m.name, o.orderDate, o.orderStatus, d.address)" +
                        " from Order o" +
                        " join o.member m" +
                        " join o.delivery d", OrderJpaDirectDto.class
        ).getResultList();

        return resultDtoList;
    }
}
