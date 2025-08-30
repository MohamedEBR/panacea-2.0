// src/test/java/com/example/panacea/services/MemberServiceTest.java
package com.example.panacea.servicetests;

import com.example.panacea.enums.MemberStatus;
import com.example.panacea.exceptions.MemberNotFoundException;
import com.example.panacea.models.Member;
import com.example.panacea.models.Student;
import com.example.panacea.repo.MemberRepository;
import com.example.panacea.services.MemberService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Collections;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class MemberServiceTest {

    @Mock
    private MemberRepository memberRepository;



    @InjectMocks
    private MemberService memberService;


    @Test
    void getMemberById_MemberExists_ReturnsMember() {
        Member member = new Member();
        member.setId(1L);
        when(memberRepository.findById(1L)).thenReturn(Optional.of(member));

        Member result = memberService.getMemberById(1L);
        assertEquals(1L, result.getId());
        verify(memberRepository).findById(1L);
    }

    @Test
    void getMemberById_MemberNotFound_Throws() {
        when(memberRepository.findById(2L)).thenReturn(Optional.empty());
        assertThrows(MemberNotFoundException.class, () -> memberService.getMemberById(2L));
    }

    @Test
    void freezeMember_SetsStatusFrozen() {
        Member member = new Member();
        member.setId(3L);
        member.setStatus(MemberStatus.ACTIVE);
        when(memberRepository.findById(3L)).thenReturn(Optional.of(member));

        memberService.freezeMember(3L);
        assertEquals(MemberStatus.FROZEN, member.getStatus());
        verify(memberRepository).save(member);
    }

    @Test
    void unfreezeMember_SetsStatusActive() {
        Member member = new Member();
        member.setId(4L);
        member.setStatus(MemberStatus.FROZEN);
        when(memberRepository.findById(4L)).thenReturn(Optional.of(member));


        memberService.unfreezeMember(4L);
        assertEquals(MemberStatus.ACTIVE, member.getStatus());
        verify(memberRepository).save(member);
    }

    @Test
    void updateMemberStatus_SuspendsWhenNoStudents() {
        Member member = new Member();
        member.setStatus(MemberStatus.ACTIVE);
        member.setStudents(Collections.emptyList());

        memberService.updateMemberStatus(member);
        assertEquals(MemberStatus.SUSPENDED, member.getStatus());
    }

    @Test
    void updateMemberStatus_ReactivatesWhenHasStudents() {
        Member member = new Member();
        member.setStatus(MemberStatus.SUSPENDED);
        Student stubStudent = new Student();
        member.setStudents(Collections.singletonList(stubStudent));

        memberService.updateMemberStatus(member);
        assertEquals(MemberStatus.ACTIVE, member.getStatus());
    }
}
