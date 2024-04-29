package jpa.commerce.repository;

import jpa.commerce.domain.Member;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface MemberRepositorySpringDataJpa extends JpaRepository<Member, Long> {

    List<Member> findMemberByName(String name);
}
