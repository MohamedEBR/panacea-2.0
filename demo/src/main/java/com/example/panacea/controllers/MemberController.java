package com.example.panacea.controllers;

import com.example.panacea.dto.UpdateMemberInfoRequest;
import com.example.panacea.dto.UpdatePasswordRequest;
import com.example.panacea.dto.UpdateStudentsRequest;
import com.example.panacea.dto.UpdatePaymentMethodRequest;
import com.example.panacea.models.Member;
import com.example.panacea.models.Student;
import com.example.panacea.services.MemberService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/members")
@RequiredArgsConstructor
public class MemberController {

    private final MemberService service;

    @GetMapping("/{id}")
    public ResponseEntity<Member> getMemberById(@PathVariable Long id) {
        Member member = service.getMemberById(id);
        return ResponseEntity.ok(member);
    }

    @GetMapping("/{id}/students")
    public ResponseEntity<List<Student>> getMemberStudents(@PathVariable Long id) {
        List<Student> students = service.getMemberStudents(id);
        return ResponseEntity.ok(students);
    }

    @PutMapping("/{id}/info")
    public ResponseEntity<String> updateMemberInfo(
            @PathVariable Long id,
            @RequestBody UpdateMemberInfoRequest request
    ) {
        service.updateMemberInfo(id, request);
        return ResponseEntity.ok("Member info updated successfully");
    }

    @PutMapping("/{id}/password")
    public ResponseEntity<String> updatePassword(
            @PathVariable Long id,
            @RequestBody UpdatePasswordRequest request
    ) {
        service.updatePassword(id, request);
        return ResponseEntity.ok("Password updated successfully");
    }

    @PutMapping("/{id}/students")
    public ResponseEntity<String> updateStudents(
            @PathVariable Long id,
            @RequestBody UpdateStudentsRequest request
    ) {
        service.updateStudents(id, request);
        return ResponseEntity.ok("Students updated successfully");
    }

    @PutMapping("/{id}/payment-method")
    public ResponseEntity<String> updatePaymentMethod(
            @PathVariable Long id,
            @RequestBody UpdatePaymentMethodRequest request
    ) {
        service.updatePaymentMethod(id, request);
        return ResponseEntity.ok("Payment method updated successfully");
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<String> cancelMembership(@PathVariable Long id) {
        service.cancelMembership(id);
        return ResponseEntity.ok("Membership cancelled successfully");
    }
}
