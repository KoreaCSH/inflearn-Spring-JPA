package jpabook.jpapractice.api;

import jpabook.jpapractice.domain.Address;
import jpabook.jpapractice.domain.Order;
import jpabook.jpapractice.domain.OrderStatus;
import jpabook.jpapractice.repository.OrderRepository;
import jpabook.jpapractice.repository.OrderSearch;
import jpabook.jpapractice.repository.order.simplequery.OrderSimpleQueryDto;
import jpabook.jpapractice.repository.order.simplequery.OrderSimpleQueryRepository;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

// XToOne (ManyToOne, OneToOne) 에서의 성능 최적화
// Order
// Order -> Member
// Order -> Delivery

@RestController
@RequiredArgsConstructor
public class OrderSimpleApiController {

    private final OrderRepository orderRepository;
    private final OrderSimpleQueryRepository orderSimpleQueryRepository;

    // 쿼리 방식 선택 권장 순서
    // 1. 우선 Entity 를 Dto 로 변환하는 방법 선택 (V2)
    // 2. 필요하면 fetch join 으로 성능 최적화 (V3) / 대부분의 성능 이슈 해결 가능
    // 3. 그래도 안된다면 JPA 에서 Dto 를 직접 조회하는 방법 사용 (V4)
    // 4. 최후의 방법은 JPA 가 제공하는 네이티브 SQL이나 스프링 JDBC Template 를 사용해서 SQL을 직접 사용.
    @GetMapping("/api/v1/simple-orders")
    public List<Order> ordersV1() {
        List<Order> findOrders = orderRepository.findAll(new OrderSearch());
        return findOrders;
    }

    @GetMapping("/api/v2/simple-orders")
    public Result ordersV2() {
        // V1 과 V2 에서는 N+1 문제가 발생한다.
        List<SimpleOrderDto> collect = orderRepository.findAll(new OrderSearch())
                .stream()
                .map(o -> new SimpleOrderDto(o))
                .collect(Collectors.toList());

        return new Result(collect);
    }

    @GetMapping("/api/v3/simple-orders")
    public Result ordersV3() {
        // fetch join 활용
        // [ Entity 를 Dto 로 변환 ]
        List<Order> orders = orderRepository.findAllWithMemberDelivery();
        List<SimpleOrderDto> collect = orders.stream().map(o -> new SimpleOrderDto(o)).collect(Collectors.toList());
        return new Result(collect);
    }

    @GetMapping("/api/v4/simple-orders")
    public Result ordersV4() {
        // [ JPA 에서 DTO를 바로 조회 ]
        // fetch join 은 모든 column 을 select 하는 반면,
        // v4는 select 하는 쿼리가 줄어든다. (네트워크 용량 최적화할 수 있으나 생각보다 미비하다)
        // 하지만 코드 간결성과 코드 재사용성 측면에서는 fetch join 이 더 낫다.
        List<OrderSimpleQueryDto> orderDtos = orderSimpleQueryRepository.findOrderDtos();
        return new Result(orderDtos);
    }

    @Data
    @AllArgsConstructor
    static class Result<T> {
        private T data;
    }

    @Data
    static class SimpleOrderDto {
        private Long orderId;
        private String name;
        private LocalDateTime orderDate;
        private OrderStatus orderStatus;
        private Address address;

        public SimpleOrderDto(Order order) {
            this.orderId = order.getId();
            this.name = order.getMember().getName();
            this.orderDate = order.getOrderDate();
            this.orderStatus = order.getStatus();
            this.address = order.getDelivery().getAddress();
        }
    }

}
