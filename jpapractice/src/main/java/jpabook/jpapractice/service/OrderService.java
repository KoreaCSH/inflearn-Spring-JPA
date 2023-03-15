package jpabook.jpapractice.service;

import jpabook.jpapractice.domain.Delivery;
import jpabook.jpapractice.domain.Member;
import jpabook.jpapractice.domain.Order;
import jpabook.jpapractice.domain.OrderItem;
import jpabook.jpapractice.domain.item.Item;
import jpabook.jpapractice.repository.ItemRepository;
import jpabook.jpapractice.repository.MemberRepository;
import jpabook.jpapractice.repository.OrderRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class OrderService {

    private final OrderRepository orderRepository;
    private final MemberRepository memberRepository;
    private final ItemRepository itemRepository;
    /*
    * 주문
    */

    @Transactional
    public Long order(Long memberId, Long itemId, int count) {
        // 엔티티 조회
        Member member = memberRepository.findOne(memberId);
        Item item = itemRepository.findOne(itemId);

        // 배송정보 생성
        Delivery delivery = new Delivery();
        delivery.setAddress(member.getAddress());

        // 주문상품 생성
        OrderItem orderItem = OrderItem.createOrderItem(item, item.getPrice(), count);

        // Order만 Delivery와 OrderItem을 참조하고 있기 때문에 cascade 를 ALL 로 설정할 수 있었다.
        // 만약 복잡하게 얽혀있다면 Delivery 와 OrderItem 의 repository 를 따로 만들어서 각각 persist 해야 할 것이다.
        // 처음에 헷갈린다면 일단 다 persist() 하도록 설계하고, 그 뒤에 refactoring 하자.

        // 주문 생성
        Order order = Order.createOrder(member, delivery, orderItem);

        // 주문 저장
        orderRepository.save(order);
        return order.getId();
    }

    /*
    * 취소
     */
    public void cancelOrder(Long orderId) {
        // 취소할 주문 조회
        Order order = orderRepository.findOne(orderId).
                orElseThrow(() -> new IllegalStateException("없는 주문입니다."));

        // 주문 취소 로직은 delete 문을 날리는 것이 아니라 status 를 cancel 로 변경하는 것이다.
        // 취소된 주문 기록도 남겨야하므로.
        order.cancel();
    }

    // 검색
//    public List<Order> findOrders(OrderSearch orderSearch) {
//        return orderRepository.findAll(orderSearch);
//    }

}
