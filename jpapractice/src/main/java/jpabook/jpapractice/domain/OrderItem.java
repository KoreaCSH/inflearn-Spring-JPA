package jpabook.jpapractice.domain;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jpabook.jpapractice.domain.item.Item;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;

@Entity
@Getter @Setter
public class OrderItem {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "order_item_id")
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "item_id")
    private Item item;

    @JsonIgnore
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "order_id") // FK 설정을 @JoinColumn 으로 한다.
    private Order order;

    private int orderPrice; // 주문 가격
    private int count; // 주문 수량

    protected OrderItem() {
        // JPA는 protected까지 허용.
        // 막아두지 않는다면 누구는 생성 메서드로 OrderItem을 생성하고, 다른 누군가는 생성자로 OrderItem을 생성할 수도 있을 것.
        // 그것을 방지하기 위해 기본 생성자를 정의하자.
    }

    // 생성 메서드
    public static OrderItem createOrderItem(Item item, int orderPrice, int count) {
        OrderItem orderItem = new OrderItem();
        orderItem.setItem(item);
        orderItem.setOrderPrice(orderPrice);
        orderItem.setCount(count);

        // 넘어오면서 해당 item 의 재고를 감소해야 한다.
        item.removeStock(count);
        return orderItem;
    }

    // 비즈니스 로직
    public void cancel() {
        getItem().addStock(count);
    }

    // 조회 로직 - 주문 상품 전체 가격 조회
    public int getTotalPrice() {
        return getOrderPrice() * getCount();
    }
}
