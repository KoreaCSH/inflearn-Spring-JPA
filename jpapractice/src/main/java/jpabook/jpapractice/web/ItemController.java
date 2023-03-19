package jpabook.jpapractice.web;

import jpabook.jpapractice.domain.item.Book;
import jpabook.jpapractice.domain.item.Item;
import jpabook.jpapractice.service.ItemService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;

import java.util.List;

@Controller
@RequiredArgsConstructor
public class ItemController {

    private final ItemService itemService;

    @GetMapping("/items/new")
    public String createForm(Model model) {
        model.addAttribute("form", new BookForm());
        return "items/createItemForm";
    }

    @PostMapping("/items/new")
    public String create(BookForm bookForm) {

        // createBook() - create 메서드를 만들어서 book 객체 생성하는 것이 좋다.
        // 즉, setter 를 없애는 것이 좋다.
        Book book = new Book();
        book.setName(bookForm.getName());
        book.setPrice(bookForm.getPrice());
        book.setStockQuantity(bookForm.getStockQuantity());
        book.setAuthor(bookForm.getAuthor());
        book.setIsbn(bookForm.getIsbn());

        itemService.saveItem(book);
        return "redirect:/items";
    }

    @GetMapping("/items")
    public String list(Model model) {
        List<Item> items = itemService.findItems();
        model.addAttribute("items", items);
        return "items/itemList";
    }

    @GetMapping("/items/{itemId}/edit")
    public String updateItemForm(@PathVariable("itemId") Long itemId, Model model) {
        // 사실 casting 해서 사용하는 것이 좋진 않으나, 예제를 단순하게 하기 위해 Book 으로 받고 casting 했다.
        // pathVariable 넘길 때 url 로 권한이 없는 사람이 접근할 수도 있다.
        // 그러므로 해당 url 요청 시 현재 로그인한 사람이 권한이 있는 사람인지 체크할 필요가 있다.

        Book item = (Book)itemService.findOne(itemId);

        BookForm bookForm = new BookForm();

        bookForm.setId(item.getId());
        bookForm.setName(item.getName());
        bookForm.setPrice(item.getPrice());
        bookForm.setStockQuantity(item.getStockQuantity());
        bookForm.setAuthor(item.getAuthor());
        bookForm.setIsbn(item.getIsbn());

        model.addAttribute("form", bookForm);
        return "items/updateItemForm";
    }

    @PostMapping("/items/{itemId}/edit")
    public String updateItem(@PathVariable("itemId") Long itemId, @ModelAttribute("form") BookForm form) {

        // "준영속 엔티티" 에 대해 정확히 알고 있어야 한다.
        // 준영속 엔티티는 영속성 컨텍스트가 관리하지 않는 엔티티이다. Book book = new Book(); 등.
        // 준영속 엔티티 수정하는 2가지 방법
        // 1. 변경 감지 기능 (dirty checking) - 변경할 값을 내가 선택할 수 있다 : 이것이 더 나은 방법
        // 2. 병합 (merge) 사용 - 병합 시 변경할 값이 없으면 그 값을 null 로 변경한다는 치명적인 단점이 있다.

        // Book book = new Book();

        // book.setId(form.getId());
        // book.setName(form.getName());
        // book.setPrice(form.getPrice());
        // book.setStockQuantity(form.getStockQuantity());
        // book.setAuthor(form.getAuthor());
        // book.setIsbn(form.getIsbn());

        // itemService.saveItem(book);

        itemService.updateItem(itemId, form.getName(), form.getPrice(), form.getStockQuantity());
        return "redirect:/items";
    }

}
