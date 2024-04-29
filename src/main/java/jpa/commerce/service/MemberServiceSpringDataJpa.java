package jpa.commerce.service;

import jpa.commerce.domain.Address;
import jpa.commerce.domain.Member;
import jpa.commerce.repository.MemberRepositorySpringDataJpa;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor // @Autowired보다 이 어노테이션을 통한 생성자 주입이 더 효율적이다.
public class MemberServiceSpringDataJpa {

    private final MemberRepositorySpringDataJpa memberRepositorySpringDataJpa;

    /**
     * 회원가입
     */
    @Transactional
    public Long registMember(Member member) {
        isDuplicateMember(member);
        memberRepositorySpringDataJpa.save(member);
        return member.getId();
    }

    private void isDuplicateMember(Member member) {
        List<Member> findMemberNames = memberRepositorySpringDataJpa.findMemberByName(member.getName());
        if (!findMemberNames.isEmpty()) { // 있는지 없는지 판단 로직은 리포지토리 sql 쿼리문에 있다.
            throw new IllegalStateException("중복 회원입니다.");
        }
    }

    /**
     * 전체 회원 조회
     */
    public List<Member> findAllMembers() {
        return memberRepositorySpringDataJpa.findAll();
    }

    /**
     * 개별 회원 조회 - id값 사용
     */
    public Member findMemberById(Long memberId) {
        return memberRepositorySpringDataJpa.findById(memberId).get();
    }

    @Transactional
    public void updateMember(Long memberId, String name, String country, String city, String zipcode) {
        Member member = memberRepositorySpringDataJpa.findById(memberId).get();
        Address address = new Address(country, city, zipcode);

        member.setName(name);
        member.setAddress(address);
    }
}
