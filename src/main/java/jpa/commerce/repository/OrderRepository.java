package jpa.commerce.repository;

import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.jpa.impl.JPAQueryFactory;
import jakarta.persistence.EntityManager;
import jpa.commerce.domain.*;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;
import org.springframework.util.StringUtils;

import java.util.List;

import static jpa.commerce.domain.QMember.member;

@Repository
@RequiredArgsConstructor
public class OrderRepository {

    private final EntityManager em;

    public void regist(Order order) {
        em.persist(order);
    }

    public Order findOrderById(Long id) {
        return em.find(Order.class, id);
    }

    public List<Order> findAllOrders(SearchOption searchOption) {
        JPAQueryFactory query = new JPAQueryFactory(em);
        QOrder order = QOrder.order;
        QMember member = QMember.member;

        return query
                .select(order)
                .from(order)
                .join(order.member, member)
                .where(findByName(searchOption.getMemberName()), findByStatus(searchOption.getOrderStatus()))
                .limit(100)
                .fetch();
    }

    private static BooleanExpression findByName(String name) {
        if (!StringUtils.hasText(name)) {
            return null;
        }
        return member.name.like(name);
    }

    private static BooleanExpression findByStatus(OrderStatus orderStatus) {
        if (orderStatus == null) {
            return null;
        }
        return QOrder.order.orderStatus.eq(orderStatus);
    }

    public List<Order> findAllUsingMemberDelivery() {
        List<Order> resultOrders = em.createQuery(
                "select o from Order o" +
                        " join fetch o.member m" +
                        " join fetch o.delivery d", Order.class
        ).getResultList();
        return resultOrders;
    }

}
