package jpabook.jpapractice.service;

import jpabook.jpapractice.domain.Address;
import jpabook.jpapractice.domain.Member;
import jpabook.jpapractice.domain.Order;
import jpabook.jpapractice.domain.OrderStatus;
import jpabook.jpapractice.domain.item.Book;
import jpabook.jpapractice.domain.item.Item;
import jpabook.jpapractice.exception.NotEnoughStockException;
import jpabook.jpapractice.repository.OrderRepository;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;

import java.util.Optional;

import static org.assertj.core.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@Transactional
class OrderServiceTest {

    @Autowired EntityManager em;
    @Autowired OrderService orderService;
    @Autowired OrderRepository orderRepository;

    @Test
    void 상품주문() {
        // given
        Member member = new Member();
        member.setName("회원1");
        member.setAddress(new Address("서울", "강남", "123-123"));
        em.persist(member);

        Book book = new Book();
        book.setName("JPA");
        book.setPrice(10000);
        book.setStockQuantity(10);
        em.persist(book);

        int orderCount = 2;

        // when
        Long orderId = orderService.order(member.getId(), book.getId(), orderCount);

        // then
        Order findOrder = orderRepository.findOne(orderId).get();
        assertThat(findOrder.getStatus()).isEqualTo(OrderStatus.ORDER);
        assertThat(findOrder.getOrderItems().size()).isEqualTo(1);
        assertThat(findOrder.getTotalPrice()).isEqualTo(10000 * orderCount);
        assertThat(book.getStockQuantity()).isEqualTo(8);

    }

    @Test
    void 주문취소() {
        // given
        Member member = new Member();
        member.setName("회원1");
        member.setAddress(new Address("서울", "강남", "123-123"));
        em.persist(member);

        Book book = new Book();
        book.setName("JPA");
        book.setPrice(10000);
        book.setStockQuantity(10);
        em.persist(book);

        int orderCount = 2;
        Long orderId = orderService.order(member.getId(), book.getId(), orderCount);

        // when
        orderService.cancelOrder(orderId);

        // then
        Order findOrder = orderRepository.findOne(orderId).get();

        assertThat(findOrder.getStatus()).isEqualTo(OrderStatus.CANCEL);
        // 주문 취소 시 재고 증가
        assertThat(book.getStockQuantity()).isEqualTo(10);
    }

    @Test
    void 상품주문_재고주문수량초과() {
        // given
        Member member = new Member();
        member.setName("회원1");
        member.setAddress(new Address("서울", "강남", "123-123"));
        em.persist(member);

        Book book = new Book();
        book.setName("JPA");
        book.setPrice(10000);
        book.setStockQuantity(10);
        em.persist(book);

        int orderCount = 12;

        // when
        assertThrows(NotEnoughStockException.class,
                () -> orderService.order(member.getId(), book.getId(), orderCount));
    }

}