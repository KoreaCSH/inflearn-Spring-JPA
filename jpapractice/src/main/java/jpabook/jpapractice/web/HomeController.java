package jpabook.jpapractice.web;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

// lombok 으로 slf4j logger 를 편하게 사용할 수 있다.

@Controller
@Slf4j
public class HomeController {

    @RequestMapping("/")
    public String home() {

        log.info("home controller");
        return "home";
    }

}
