package jpabook.jpapractice.service;

import jpabook.jpapractice.domain.Member;
import jpabook.jpapractice.repository.MemberRepository;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@Transactional
class MemberServiceTest {

    @Autowired
    MemberService memberService;

    @Autowired
    MemberRepository memberRepository;

    @Test
    void 회원가입() {
        // given
        Member member = new Member();
        member.setName("kim");

        // when
        memberService.join(member);

        // then
        Member findMember = memberService.findOnd(member.getId());
        Assertions.assertThat(member).isEqualTo(findMember);
    }

    @Test
    void 중복_회원_예외() {
        // given
        Member member1 = new Member();
        member1.setName("kim");

        Member member2 = new Member();
        member2.setName("kim");

        // when
        memberService.join(member1);

        // then
        assertThrows(IllegalStateException.class,
                () -> memberService.join(member2));
    }

}