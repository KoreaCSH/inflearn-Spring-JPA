package jpabook.jpapractice.domain.item;

import jpabook.jpapractice.domain.Category;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity
@Inheritance(strategy = InheritanceType.SINGLE_TABLE) // 전략이 3가지 있다.
@DiscriminatorColumn(name = "dtype")
@Getter @Setter
public abstract class Item {

    @Id
    @GeneratedValue
    @Column(name = "item_id")
    private Long id;

    private String name;
    private int price;
    private int stockQuantity;

    // 원래 manytomany는 사용하지 않는 것이 좋다. db 이론처럼 새로운 테이블을 만드는 것이 정석. 하지만 만약 사용한다면 JoinTable을 사용해야 한다.
    @ManyToMany(mappedBy = "items")
    private List<Category> categories = new ArrayList<>();

}
