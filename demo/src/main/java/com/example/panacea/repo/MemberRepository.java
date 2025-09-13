package com.example.panacea.repo;

import com.example.panacea.models.Member;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;

public interface MemberRepository extends JpaRepository<Member, Long> {

    Optional<Member> findByEmail(String email);
    Optional<Member> findByStudents_Id(Long studentId);

    @Query("SELECT m FROM Member m WHERE m.status = com.example.panacea.enums.MemberStatus.ACTIVE")
    List<Member> findActiveMembers();
}
