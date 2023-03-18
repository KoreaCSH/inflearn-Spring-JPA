package jpabook.jpapractice.web;

import jpabook.jpapractice.domain.Address;
import jpabook.jpapractice.domain.Member;
import jpabook.jpapractice.service.MemberService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;

import javax.validation.Valid;
import java.util.List;

@Controller
@RequiredArgsConstructor
public class MemberController {

    private final MemberService memberService;

    @GetMapping("members/new")
    public String createForm(Model model) {
        model.addAttribute("memberForm", new MemberForm());
        return "members/createMemberForm";
    }

    @PostMapping("members/new")
    public String create(@Valid MemberForm memberForm, BindingResult result) {

        // Validator interface 는 Errors 와 연결되고, @Valid 는 BindingResult(Errors 를 상속받은 인터페이스) 와 연결되는구나

        if (memberService.checkDuplicateMemberName(memberForm.getName())) {
            result.rejectValue("name", "duplicate", "이미 가입된 이름입니다.");
        }

        if(result.hasErrors()) {
            return "members/createMemberForm";
        }

        Address address = new Address(memberForm.getCity(), memberForm.getStreet(), memberForm.getZipcode());

        Member member = new Member();
        member.setName(memberForm.getName());
        member.setAddress(address);

        memberService.join(member);
        return "redirect:/";

    }

    @GetMapping("members")
    public String list(Model model) {
        List<Member> members = memberService.findMembers();
        // 실무에서 로직이 복잡해지면 Member 를 MemberDto 로 변환해서 필요한 데이터만 보내는 것이 좋다.
        // API 를 만들 때에는 이유를 불문하고 절대 Entity 를 웹으로 반환하면 안된다.
        model.addAttribute("members", members);
        return "members/memberList";
    }

}
