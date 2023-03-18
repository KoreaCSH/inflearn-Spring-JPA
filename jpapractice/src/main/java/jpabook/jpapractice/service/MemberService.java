package jpabook.jpapractice.service;

import jpabook.jpapractice.domain.Member;
import jpabook.jpapractice.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class MemberService {

    private final MemberRepository memberRepository;

    /*
    * 회원가입
    * */
    @Transactional
    public Long join(Member member) {
        // 중복회원 검증
        validateDuplicateMember(member);

        memberRepository.save(member);
        return member.getId();
    }

    // 최후 테스트를 위해 name을 unique key로 설정하는 것이 좋음.
    private void validateDuplicateMember(Member member) {
        List<Member> findMembers = memberRepository.findByName(member.getName());
        if(!findMembers.isEmpty()) {
            throw new IllegalStateException("이미 존재하는 회원입니다");
        }
    }

    // 회원 전체 조회
    public List<Member> findMembers() {
        return memberRepository.findAll();
    }

    public Member findOnd(Long memberId) {
        return memberRepository.findOne(memberId);
    }

    public boolean checkDuplicateMemberName(String name) {
        List<Member> findMembers = memberRepository.findByName(name);
        if(!findMembers.isEmpty()) {
            return true;
        }
        return false;
    }

}
