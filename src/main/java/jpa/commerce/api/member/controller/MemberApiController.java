package jpa.commerce.api.member.controller;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotEmpty;
import jpa.commerce.api.member.dto.*;
import jpa.commerce.domain.Member;
import jpa.commerce.service.MemberServiceSpringDataJpa;
import lombok.AllArgsConstructor;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequiredArgsConstructor
public class MemberApiController {

    //== Dependency Injection ==//
    private final MemberServiceSpringDataJpa memberServiceSpringDataJpa;

    //=================== Mapper ====================//
    @GetMapping("/api/v1/members")
    public List<Member> memberListV1() {
        return memberServiceSpringDataJpa.findAllMembers();
    } // json 형태로 쫙 바껴서 리스트가 출력될 것이다.

    @GetMapping("/api/v2/members")
    public CustomFormat memberListV2() {
        List<Member> allMembers = memberServiceSpringDataJpa.findAllMembers();
        List<MemberDto> memberDtoList = allMembers.stream()
                .map(m -> new MemberDto(m.getName()))
                .collect(Collectors.toList());

        return new CustomFormat(memberDtoList);
    }

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
}
