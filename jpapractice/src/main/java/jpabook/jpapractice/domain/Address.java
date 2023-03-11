package jpabook.jpapractice.domain;

import lombok.Getter;

import javax.persistence.Embeddable;

@Embeddable
@Getter
public class Address {

    private String city;
    private String street;
    private String zipcode;

    protected Address() {

    }

    // 값 타입은 변경 불가능하게 설계해야 한다.
    // @Setter 를 달지 말고, 생성자에서 값을 모두 초기화해서 변경 불가능한 클래스로 만들자.
    // 또한, JPA 구현 라이브러리가 리플랙션과 같은 기술을 사용할 수 있도록 기본 생성자를 선언해야 하는데,
    // 보통 접근 제어자를 protected 로 설정한다.

    public Address(String city, String street, String zipcode) {
        this.city = city;
        this.street = street;
        this.zipcode = zipcode;
    }
}
