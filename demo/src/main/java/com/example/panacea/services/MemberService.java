package com.example.panacea.services;

import com.example.panacea.dto.UpdateMemberInfoRequest;
import com.example.panacea.dto.UpdatePasswordRequest;
import com.example.panacea.dto.UpdatePaymentMethodRequest;
import com.example.panacea.dto.UpdateStudentsRequest;
import com.example.panacea.enums.Belt;
import com.example.panacea.enums.Gender;
import com.example.panacea.enums.MemberStatus;
import com.example.panacea.exceptions.*;
import com.example.panacea.models.Member;
import com.example.panacea.models.Program;
import com.example.panacea.models.Student;
import com.example.panacea.repo.MemberRepository;
import com.example.panacea.repo.ProgramRepository;
import com.example.panacea.repo.StudentRepository;
import com.stripe.exception.StripeException;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.sql.Date;
import java.util.List;

@Service
@RequiredArgsConstructor
public class MemberService {

    private final MemberRepository memberRepository;
    private final PasswordEncoder passwordEncoder;
    private final ProgramRepository programRepository;
    private final StudentRepository studentRepository;
    private final StripeService stripeService;

    public Member getMemberById(long id) {
        return memberRepository.findById(id)
                .orElseThrow(() -> new MemberNotFoundException("Member not found with id: " + id));
    }

    public List<Student> getMemberStudents(long memberId) {
        Member member = memberRepository.findById(memberId)
                .orElseThrow(() -> new MemberNotFoundException("Member not found with id: " + memberId));

        return member.getStudents();
    }

    public void updateMemberInfo(long id, UpdateMemberInfoRequest request) {
        Member member = memberRepository.findById(id)
                .orElseThrow(() -> new MemberNotFoundException("Member not found with id: " + id));

        member.setName(request.getName());
        member.setPhone(request.getPhone());
        member.setAddress(request.getAddress());
        member.setDob(request.getDob());
        member.setLastName(request.getLastName());
        member.setPostalCode(request.getPostalCode());
        member.setCity(request.getCity());

        memberRepository.save(member);
    }

    public void updatePassword(long id, UpdatePasswordRequest request) {
        Member member = memberRepository.findById(id)
                .orElseThrow(() -> new MemberNotFoundException("Member not found with id: " + id));

        if (!passwordEncoder.matches(request.getOldPassword(), member.getPassword())) {
            throw new InvalidOldPasswordException("Current password is incorrect.");
        }

        if (!request.getNewPassword().equals(request.getConfirmPassword())) {
            throw new PasswordMismatchException("New password and confirmation do not match.");
        }

        member.setPassword(passwordEncoder.encode(request.getNewPassword()));
        memberRepository.save(member);
    }

    @Transactional
    public void updateStudents(long memberId, UpdateStudentsRequest request) {
        Member member = memberRepository.findById(memberId)
                .orElseThrow(() -> new MemberNotFoundException("Member not found with id: " + memberId));

        List<Student> existingStudents = member.getStudents();

        List<Student> updatedStudents = request.getStudents().stream().map(s -> {
            List<Program> programs = programRepository.findAllById(s.getProgramIds());

            if (programs.size() != s.getProgramIds().size()) {
                throw new ProgramNotFoundException("One or more program IDs are invalid for student: " + s.getName());
            }

            if (s.getProgramIds().size() > 5) {
                throw new TooManyProgramsException("Student " + s.getName() + " cannot be enrolled in more than 5 programs.");
            }

            Student student = new Student();
            student.setName(s.getName());
            student.setDob(s.getDob());
            student.setWeight(s.getWeight());
            student.setHeight(s.getHeight());
            student.setMedicalConcerns(s.getMedicalConcerns());
            student.setGender(Gender.valueOf(s.getGender().toUpperCase()));
            student.setBelt(Belt.valueOf(s.getBelt().toUpperCase()));
            student.setYearsInClub(s.getYearsInClub());
            student.setPrograms(programs);
            student.setMember(member);

            return student;
        }).toList();

        studentRepository.deleteAll(existingStudents);

        member.setStudents(updatedStudents);
        memberRepository.save(member);
    }

    public void updatePaymentMethod(Long memberId, UpdatePaymentMethodRequest request) {
        Member member = memberRepository.findById(memberId)
                .orElseThrow(() -> new MemberNotFoundException("Member not found with id: " + memberId));

        String stripeCustomerId = member.getStripeCustomerId();
        if (stripeCustomerId == null || stripeCustomerId.isBlank()) {
            throw new StripeIntegrationException("No Stripe customer ID found for member. Cannot update payment method.");
        }

        try {
            stripeService.attachPaymentMethodToCustomer(stripeCustomerId, request.getPaymentMethodId());
        } catch (StripeException e) {
            throw new StripeIntegrationException("Stripe payment method update failed: " + e.getMessage());
        }
    }

    @Transactional
    public void cancelMembership(long id) throws StripeException {
        Member member = memberRepository.findById(id)
                .orElseThrow(() -> new MemberNotFoundException("Member not found with id: " + id));

        member.setStatus(MemberStatus.CANCELLED);
        memberRepository.save(member);

        stripeService.cancelCustomerSubscription(member.getStripeCustomerId());

    }

    public void updateMemberStatus(Member member) {
        if (member == null) return;

        boolean hasStudents = member.getStudents() != null && !member.getStudents().isEmpty();

        switch (member.getStatus()) {
            case ACTIVE -> {
                if (!hasStudents) {
                    member.setStatus(MemberStatus.SUSPENDED);
                }
            }
            case SUSPENDED -> {
                if (hasStudents) {
                    member.setStatus(MemberStatus.ACTIVE);
                }
            }
        }

        memberRepository.save(member);
    }


}
