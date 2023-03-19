package jpabook.jpapractice.service;

import jpabook.jpapractice.domain.item.Book;
import jpabook.jpapractice.domain.item.Item;
import jpabook.jpapractice.repository.ItemRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class ItemService {

    private final ItemRepository itemRepository;

    @Transactional
    public void saveItem(Item item) {
        itemRepository.save(item);
    }

    @Transactional
    public void updateItem(Long itemId, String name, int price, int stockQuantity) {

        Item findItem = itemRepository.findOne(itemId);

        // findItem 은 영속성 컨텍스트가 관리하는 영속 엔티티이므로 em.save() 를 날릴 필요가 없다.
        // dirty checking 으로 변경된다.
        // 또한, 값을 setter 로 변경하는 것보단 의미있는 메서드를 정의하여 사용하는 것이 좋다.
        findItem.setName(name);
        findItem.setPrice(price);
        findItem.setStockQuantity(stockQuantity);
    }

    public List<Item> findItems() {
        return itemRepository.findAll();
    }

    public Item findOne(Long itemId) {
        return itemRepository.findOne(itemId);
    }

}
