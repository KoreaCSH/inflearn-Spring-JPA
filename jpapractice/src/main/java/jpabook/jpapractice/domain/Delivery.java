package jpabook.jpapractice.domain;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;

@Entity
@Getter
@Setter
public class Delivery {

    @Id
    @GeneratedValue
    @Column(name = "delivery_id")
    private Long id;

    @OneToOne(mappedBy = "delivery", fetch = FetchType.LAZY)
    private Order order;

    @Embedded
    private Address address;

    @Enumerated(EnumType.STRING) // enum 을 넣을 때는 @Enumerated 를 추가해야 한다. ordinal 은 0 , 1, 2 .. 상태 변경될 수 있으므로 STRING 으로 설정하자.
    private DeliveryStatus status; // READY, COMP

}
