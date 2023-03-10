package jpabook.jpapractice.domain;

import jpabook.jpapractice.domain.item.Item;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;

@Entity
@Getter @Setter
public class OrderItem {

    @Id @GeneratedValue
    @Column(name = "order_item_id")
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "item_id")
    private Item item;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "order_id") // FK 설정을 @JoinColumn 으로 한다.
    private Order order;

    private int orderPrice; // 주문 가격
    private int count; // 주문 수량

}
