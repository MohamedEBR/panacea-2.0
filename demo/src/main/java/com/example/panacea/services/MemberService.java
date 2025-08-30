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
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;

@Service
@RequiredArgsConstructor
public class MemberService {

    private final MemberRepository memberRepository;
    private final PasswordEncoder passwordEncoder;
    private final ProgramRepository programRepository;
    private final StudentRepository studentRepository;
    private final StripeService stripeService;
//    private final BillingService billingService;

    public Member getMemberById(long id) {
        return memberRepository.findById(id)
                .orElseThrow(() -> new MemberNotFoundException("Member not found with id: " + id));
    }

    public List<Student> getMemberStudents(long memberId) {
        Member member = memberRepository.findById(memberId)
                .orElseThrow(() -> new MemberNotFoundException("Member not found with id: " + memberId));

        return member.getStudents();
    }

    @Transactional
    public void freezeMember(Long id) {
        Member member = getMemberById(id);
        member.setStatus(MemberStatus.FROZEN);
        memberRepository.save(member);
    }

    @Transactional
    public void unfreezeMember(Long id) {
        Member member = getMemberById(id);
        MemberStatus previous = member.getStatus();
        member.setStatus(MemberStatus.ACTIVE);
        memberRepository.save(member);

        // If reactivated within first 10 days, bill immediately
        if (previous == MemberStatus.FROZEN) {
            int day = LocalDate.now().getDayOfMonth();
//            if (day <= 10) {
////                billingService.billMember(member);
//            }
        }
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
            throw new StripeIntegrationException("No Stripe customer ID found for member. Cannot update payment method.", new Exception());
        }

//        stripeService.attachPaymentMethodToCustomer(stripeCustomerId, request.getPaymentMethodId());
    }

    @Transactional
    public void cancelMembership(long id) {
        Member member = memberRepository.findById(id)
                .orElseThrow(() -> new MemberNotFoundException("Member not found with id: " + id));

        member.setStatus(MemberStatus.CANCELLED);
        memberRepository.save(member);

//        stripeService.cancelCustomerSubscription(member.getStripeCustomerId());

    }

    public void updateMemberStatus(Member member) {
        MemberStatus previous = member.getStatus();
        boolean hasStudents = member.getStudents() != null && !member.getStudents().isEmpty();

        if (hasStudents && previous == MemberStatus.SUSPENDED) {
            member.setStatus(MemberStatus.ACTIVE);
        } else if (!hasStudents && previous == MemberStatus.ACTIVE) {
            member.setStatus(MemberStatus.SUSPENDED);
        }
        // handle FROZEN → ACTIVE transition
        else if (previous == MemberStatus.FROZEN && hasStudents) {
            member.setStatus(MemberStatus.ACTIVE);

            int dayOfMonth = LocalDate.now().getDayOfMonth();
//            if (dayOfMonth <= 10) {
//                // immediately bill this member
////                billingService.billMember(member);
//            }
        }

        memberRepository.save(member);
    }


}
