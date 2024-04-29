package jpa.commerce.api;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotEmpty;
import jpa.commerce.domain.Member;
import jpa.commerce.service.MemberServiceSpringDataJpa;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
public class MemberApiController {

    private final MemberServiceSpringDataJpa memberServiceSpringDataJpa;

    //=================== Mapper ====================//

    @GetMapping("/api/v1/members")
    public List<Member> memberListV1() {
        return memberServiceSpringDataJpa.findAllMembers();
    } // json 형태로 쫙 바껴서 리스트가 출력될 것이다.


    @PostMapping("/api/v1/members") // 회원 등록을 위한 api 구현
    public RegistMemberResponse registMemberV1(@RequestBody @Valid Member member) {
        Long id = memberServiceSpringDataJpa.registMember(member);
        return new RegistMemberResponse(id);
    }

    @PostMapping("/api/v2/members")
    public RegistMemberResponse registMemberV2(@RequestBody @Valid RegistMemberRequest registMemberRequest) {

        Member member = new Member();
        member.setName(registMemberRequest.getName());
        Long id = memberServiceSpringDataJpa.registMember(member);

        return new RegistMemberResponse(id);
    }

    @PutMapping("/api/v2/members/{memberId}")
    public UpdateMemberResponse updateMemberV2(@PathVariable Long memberId,
                                               @RequestBody @Valid UpdateMemberRequest updateMemberRequest) {

        memberServiceSpringDataJpa.updateMember(memberId,
                updateMemberRequest.getName(),
                updateMemberRequest.getCountry(),
                updateMemberRequest.getCity(),
                updateMemberRequest.getZipcode());
        Member findMember = memberServiceSpringDataJpa.findMemberById(memberId);

        return new UpdateMemberResponse(findMember.getId(), findMember.getName());
    }



    //=================== DTO ====================//

    //== RegistMember ==//
    @Data
    static class RegistMemberRequest {
        @NotEmpty
        private String name;
    }

    @Data
    static class RegistMemberResponse {
        private Long id;
        public RegistMemberResponse(Long id) {
            this.id = id;
        }
    }

    //== UpdateMember ==//
    @Data
    static class UpdateMemberRequest {
        private String name;
        private String country;
        private String city;
        private String zipcode;
    }

    @Data
    // @AllArgsConstructor 생성자 대신 사용 가능
    static class UpdateMemberResponse {
        private Long id;
        private String name;

        public UpdateMemberResponse(Long id, String name) {
            this.id = id;
            this.name = name;
        }
    }


}
