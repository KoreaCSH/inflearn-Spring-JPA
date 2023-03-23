package jpabook.jpapractice.api;

import jpabook.jpapractice.domain.Address;
import jpabook.jpapractice.domain.Member;
import jpabook.jpapractice.service.MemberService;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import javax.validation.constraints.NotEmpty;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequiredArgsConstructor
public class MemberApiController {

    private final MemberService memberService;

    @PostMapping("/api/v1/members")
    public CreateMemberResponse saveMemberV1(@RequestBody @Valid Member member) {
        Long id = memberService.join(member);
        return new CreateMemberResponse(id);
    }

    @PostMapping("/api/v2/members")
    public CreateMemberResponse saveMemberV2(@RequestBody @Valid CreateMemberRequest request) {

        Member member = new Member();
        member.setName(request.getName());

        Long id = memberService.join(member);
        return new CreateMemberResponse(id);
    }

    @PutMapping("/api/v2/members/{id}")
    public UpdateMemberResponse updateMemberV2(@PathVariable("id") Long id,
                                               @RequestBody @Valid UpdateMemberRequest request) {

        Long memberId = memberService.update(id, request.getName());
        // update 메서드가 Member 를 반환하도록 할 수도 있겠지만, update 후 Member를 반환하는 것은
        // 하나의 메서드가 update도 하고 검색도 담당하게 된다.
        // 그러므로 update 메서드는 update 만 수행하도록 설계하자.
        Member findMember = memberService.findOnd(memberId);
        return new UpdateMemberResponse(findMember.getId(), findMember.getName());
    }

    @GetMapping("/api/v1/members")
    public List<Member> membersV1() {
        return memberService.findMembers();
        // entity 객체를 바로 return 할 경우, API 스펙에 포함하고 싶지 않은 필드까지 return 될 수 있다.
        // 이를 방지하기 위해 필드에 @JsonIgnore를 설정할 수도 있지만,
    }

    @GetMapping("/api/v2/members")
    public Result membersV2() {
        List<Member> findMembers = memberService.findMembers();
        List<MemberDto> collect = findMembers.stream()
                .map(m -> new MemberDto(m.getName(), m.getAddress()))
                .collect(Collectors.toList());

        return new Result(collect);
    }

    @Data
    @AllArgsConstructor
    static class Result<T> {
        private T data;
    }

    // MemberDto 의 리스트를 바로 반환하면 Json 에서는 배열로 변환된다.
    // 배열로 변환될 경우 API 스펙을 확장하는 데 한계가 있기 때문에
    // 다음처럼 Result 등의 클래스로 리스트를 한 번 감싸서 반환하는 것이 좋다.
    // 결론 : Entity 를 외부로 노출하지 말고, 반드시 API 스펙에 맞는 DTO 를 만들어서 Entity 를 DTO 로 변환한 후 반환하자!

    @Data
    @AllArgsConstructor
    static class MemberDto {
        private String name;
        private Address address;
    }

    @Data
    @AllArgsConstructor
    static class UpdateMemberResponse {
        private Long id;
        private String name;
    }

    @Data
    static class UpdateMemberRequest {
        @NotEmpty
        private String name;
    }

    @Data
    static class CreateMemberRequest {
        private String name;
    }

    @Data
    static class CreateMemberResponse {
        private Long id;

        public CreateMemberResponse(Long id) {
            this.id = id;
        }
    }

}
