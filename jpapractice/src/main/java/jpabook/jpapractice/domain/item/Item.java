package jpabook.jpapractice.domain.item;

import jpabook.jpapractice.domain.Category;
import jpabook.jpapractice.exception.NotEnoughStockException;
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

    // JPA 에서 Auto_increment 이슈를 해결하려면 GenerationType.IDENTITY 로 설정해야 한다.
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "item_id")
    private Long id;

    private String name;
    private int price;
    private int stockQuantity;

    // 원래 manytomany는 사용하지 않는 것이 좋다. db 이론처럼 새로운 테이블을 만드는 것이 정석. 하지만 만약 사용한다면 JoinTable을 사용해야 한다.
    @ManyToMany(mappedBy = "items")
    private List<Category> categories = new ArrayList<>();

    // -- 비즈니스 로직 -- Entity 자체가 해결할 수 있는 것은 Entity 에서 구현하도록 하자.
    // 재고 수량 증가 로직
    public void addStock(int quantity) {
        this.stockQuantity += quantity;
    }

    public void removeStock(int quantity) {
        int restStock = this.stockQuantity - quantity;
        if(restStock < 0) {
            throw new NotEnoughStockException("need more stock");
        }
        this.stockQuantity = restStock;
    }

}
