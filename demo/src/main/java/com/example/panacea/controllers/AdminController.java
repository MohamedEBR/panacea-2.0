package com.example.panacea.controllers;

import com.example.panacea.models.Member;
import com.example.panacea.models.Student;
import com.example.panacea.services.AdminService;
import com.example.panacea.services.MemberService;
import com.example.panacea.services.StudentService;
import com.example.panacea.services.SecurityService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/admin")
@RequiredArgsConstructor
@PreAuthorize("hasRole('SUPER_USER')")
public class AdminController {

    private final AdminService adminService;
    private final MemberService memberService;
    private final StudentService studentService;
    private final SecurityService securityService;

    @PostMapping("/trigger-monthly-billing")
    public ResponseEntity<String> triggerMonthlyBilling() {
        adminService.triggerMonthlyBilling();
        return ResponseEntity.ok("Monthly billing triggered successfully");
    }

    @GetMapping("/export-data")
    public ResponseEntity<String> exportData() {
        String filePath = adminService.exportData();
        return ResponseEntity.ok("Data exported successfully to: " + filePath);
    }

    @GetMapping("/members")
    public ResponseEntity<List<Member>> getAllMembers() {
        // Super users can access all member data
        List<Member> members = memberService.getAllMembers();
        return ResponseEntity.ok(members);
    }

    @GetMapping("/students")
    public ResponseEntity<List<Student>> getAllStudents() {
        // Super users can access all student data
        List<Student> students = studentService.getAllStudents();
        return ResponseEntity.ok(students);
    }

    @GetMapping("/members/{id}")
    public ResponseEntity<Member> getMemberById(@PathVariable Long id) {
        Member member = memberService.getMemberById(id);
        return ResponseEntity.ok(member);
    }

    @GetMapping("/students/{id}")
    public ResponseEntity<Student> getStudentById(@PathVariable Long id) {
        Student student = studentService.getStudentById(id);
        return ResponseEntity.ok(student);
    }

    @PutMapping("/members/{id}/freeze")
    public ResponseEntity<String> freezeMember(@PathVariable Long id) {
        memberService.freezeMember(id);
        securityService.logSecurityEvent("MEMBER_FROZEN", 
            securityService.getCurrentUserEmail(), 
            "Admin froze member: " + id);
        return ResponseEntity.ok("Member frozen successfully");
    }

    @PutMapping("/members/{id}/unfreeze")
    public ResponseEntity<String> unfreezeMember(@PathVariable Long id) {
        memberService.unfreezeMember(id);
        securityService.logSecurityEvent("MEMBER_UNFROZEN", 
            securityService.getCurrentUserEmail(), 
            "Admin unfroze member: " + id);
        return ResponseEntity.ok("Member unfrozen successfully");
    }

    @GetMapping("/security/audit-logs")
    public ResponseEntity<List<Map<String, Object>>> getAuditLogs(
            @RequestParam(defaultValue = "100") int limit) {
        List<Map<String, Object>> logs = securityService.getAuditLogs(limit);
        return ResponseEntity.ok(logs);
    }

    @GetMapping("/security/failed-logins")
    public ResponseEntity<List<Map<String, Object>>> getFailedLogins(
            @RequestParam(defaultValue = "50") int limit) {
        List<Map<String, Object>> failedLogins = securityService.getFailedLoginAttempts(limit);
        return ResponseEntity.ok(failedLogins);
    }

    @PostMapping("/security/unlock-account")
    public ResponseEntity<String> unlockAccount(@RequestParam String email) {
        securityService.unlockAccount(email);
        securityService.logSecurityEvent("ACCOUNT_UNLOCKED", 
            securityService.getCurrentUserEmail(), 
            "Admin unlocked account: " + email);
        return ResponseEntity.ok("Account unlocked successfully");
    }

    @GetMapping("/security/blacklisted-tokens")
    public ResponseEntity<List<String>> getBlacklistedTokens() {
        List<String> tokens = securityService.getBlacklistedTokens();
        return ResponseEntity.ok(tokens);
    }
}