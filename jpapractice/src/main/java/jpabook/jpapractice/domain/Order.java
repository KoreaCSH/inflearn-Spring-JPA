package jpabook.jpapractice.domain;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "orders")
@Getter @Setter
public class Order {

    @Id @GeneratedValue
    @Column(name = "order_id")
    private Long id;

    // ManyToOne은 기본이 즉시로딩이기 때문에 수동으로 지연로딩으로 설정해 주어야 한다.
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "member_id") // foreign_key 가 member_id로 설정된다. + 연관관계의 주인이 된다.
    private Member member;

    // Order에서 orderItem 저장 시 OrderItem 따로 저장할 필요 X
    // 즉, Order 에서 persist 하면 OrderItem 도 persist 된다.
    @OneToMany(mappedBy = "order", cascade = CascadeType.ALL)
    private List<OrderItem> orderItems = new ArrayList<>();

    @OneToOne(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    @JoinColumn(name = "delivery_id")
    private Delivery delivery;

    private LocalDateTime orderDate;

    @Enumerated(EnumType.STRING)
    private OrderStatus status; // 주문상태 [ Order, Cancle ]

    // -- 연관관계 편의 메서드 --
    // 양방향 관계일 때 사용하면 편하다. 메서드 하나로 해결 가능
    public void setMember(Member member) {
        this.member = member;
        member.getOrders().add(this);
    }

    public void addOrderItem(OrderItem orderItem) {
        orderItems.add(orderItem);
        orderItem.setOrder(this);
    }

    public void setDelivery(Delivery delivery) {
        this.delivery = delivery;
        delivery.setOrder(this);
    }

}
