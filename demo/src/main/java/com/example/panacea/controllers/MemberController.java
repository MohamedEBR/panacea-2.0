package com.example.panacea.controllers;

import com.example.panacea.dto.UpdateMemberInfoRequest;
import com.example.panacea.dto.UpdatePasswordRequest;
import com.example.panacea.dto.UpdateStudentsRequest;
import com.example.panacea.dto.UpdatePaymentMethodRequest;
import com.example.panacea.models.Member;
import com.example.panacea.models.Student;
import com.example.panacea.services.MemberService;
import com.example.panacea.services.DataOwnershipService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/members")
@RequiredArgsConstructor
@PreAuthorize("hasRole('USER')")
public class MemberController {

    private final MemberService service;
    private final DataOwnershipService dataOwnershipService;

    @GetMapping("/{id}")
    public ResponseEntity<Member> getMemberById(@PathVariable Long id) {
        dataOwnershipService.validateMemberAccess(id);
        Member member = service.getMemberById(id);
        return ResponseEntity.ok(member);
    }

    @GetMapping("/{id}/students")
    public ResponseEntity<List<Student>> getMemberStudents(@PathVariable Long id) {
        dataOwnershipService.validateMemberAccess(id);
        List<Student> students = service.getMemberStudents(id);
        return ResponseEntity.ok(students);
    }


    @PutMapping("/{id}/freeze")
    @PreAuthorize("hasRole('SUPER_USER')")
    public ResponseEntity<Void> freeze(@PathVariable Long id) {
        service.freezeMember(id);
        return ResponseEntity.ok().build();
    }

    @PutMapping("/{id}/unfreeze")
    @PreAuthorize("hasRole('SUPER_USER')")
    public ResponseEntity<Void> unfreeze(@PathVariable Long id) {
        service.unfreezeMember(id);
        return ResponseEntity.ok().build();
    }

    @PutMapping("/{id}/info")
    public ResponseEntity<String> updateMemberInfo(
            @PathVariable Long id,
            @RequestBody UpdateMemberInfoRequest request
    ) {
        dataOwnershipService.validateMemberAccess(id);
        service.updateMemberInfo(id, request);
        return ResponseEntity.ok("Member info updated successfully");
    }

    @PutMapping("/{id}/password")
    public ResponseEntity<String> updatePassword(
            @PathVariable Long id,
            @RequestBody UpdatePasswordRequest request
    ) {
        dataOwnershipService.validateMemberAccess(id);
        service.updatePassword(id, request);
        return ResponseEntity.ok("Password updated successfully");
    }

    @PutMapping("/{id}/students")
    public ResponseEntity<String> updateStudents(
            @PathVariable Long id,
            @RequestBody UpdateStudentsRequest request
    ) {
        dataOwnershipService.validateMemberAccess(id);
        service.updateStudents(id, request);
        return ResponseEntity.ok("Students updated successfully");
    }

    @PutMapping("/{id}/payment-method")
    public ResponseEntity<String> updatePaymentMethod(
            @PathVariable Long id,
            @RequestBody UpdatePaymentMethodRequest request
    ) {
        dataOwnershipService.validateMemberAccess(id);
        service.updatePaymentMethod(id, request);
        return ResponseEntity.ok("Payment method updated successfully");
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<String> cancelMembership(@PathVariable Long id) {
        dataOwnershipService.validateMemberAccess(id);
        service.cancelMembership(id);
        return ResponseEntity.ok("Membership cancelled successfully");
    }
}
