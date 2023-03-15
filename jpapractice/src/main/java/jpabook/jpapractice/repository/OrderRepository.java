package jpabook.jpapractice.repository;

import jpabook.jpapractice.domain.Order;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import java.util.List;
import java.util.Optional;

@Repository
@RequiredArgsConstructor
public class OrderRepository {

    private final EntityManager em;

    public void save(Order order) {
        em.persist(order);
    }

    public Optional<Order> findOne(Long id) {
        Order order = em.find(Order.class, id);
        return Optional.ofNullable(order);
    }

//    public List<Order> findAll(OrderSearch orderSearch) {}

}
