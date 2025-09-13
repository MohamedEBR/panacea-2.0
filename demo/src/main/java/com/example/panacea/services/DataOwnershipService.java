package com.example.panacea.services;

import com.example.panacea.models.Member;
import com.example.panacea.models.Student;
import com.example.panacea.repo.MemberRepository;
import com.example.panacea.repo.StudentRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class DataOwnershipService {
    
    private final MemberRepository memberRepository;
    private final StudentRepository studentRepository;
    private final SecurityService securityService;
    
    /**
     * Checks if current user can access member data
     */
    public boolean canAccessMemberData(Long memberId) {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        if (auth == null) return false;
        
        String currentUserEmail = auth.getName();
        
        // Super users can access all data
        if (isSuperUser(auth)) {
            return true;
        }
        
        // Regular users can only access their own data
        Member member = memberRepository.findById(memberId).orElse(null);
        if (member == null) {
            securityService.logSecurityEvent("INVALID_MEMBER_ACCESS", currentUserEmail, 
                "Attempted to access non-existent member: " + memberId);
            return false;
        }
        
        boolean canAccess = securityService.canAccessResource(currentUserEmail, member.getEmail());
        
        if (!canAccess) {
            securityService.logSecurityEvent("UNAUTHORIZED_MEMBER_ACCESS", currentUserEmail, 
                "Attempted to access member: " + memberId);
        }
        
        return canAccess;
    }
    
    /**
     * Checks if current user can access student data
     */
    public boolean canAccessStudentData(Long studentId) {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        if (auth == null) return false;
        
        String currentUserEmail = auth.getName();
        
        // Super users can access all data
        if (isSuperUser(auth)) {
            return true;
        }
        
        // Regular users can only access their own students
        Student student = studentRepository.findById(studentId).orElse(null);
        if (student == null) {
            securityService.logSecurityEvent("INVALID_STUDENT_ACCESS", currentUserEmail, 
                "Attempted to access non-existent student: " + studentId);
            return false;
        }
        
        String ownerEmail = student.getMember().getEmail();
        boolean canAccess = securityService.canAccessResource(currentUserEmail, ownerEmail);
        
        if (!canAccess) {
            securityService.logSecurityEvent("UNAUTHORIZED_STUDENT_ACCESS", currentUserEmail, 
                "Attempted to access student: " + studentId + " owned by: " + ownerEmail);
        }
        
        return canAccess;
    }
    
    /**
     * Validates if user can modify member data
     */
    public void validateMemberAccess(Long memberId) {
        if (!canAccessMemberData(memberId)) {
            throw new SecurityException("Access denied: You can only access your own member data");
        }
    }
    
    /**
     * Validates if user can modify student data
     */
    public void validateStudentAccess(Long studentId) {
        if (!canAccessStudentData(studentId)) {
            throw new SecurityException("Access denied: You can only access your own students");
        }
    }
    
    /**
     * Gets current user's member ID
     */
    public Long getCurrentUserMemberId() {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        if (auth == null) return null;
        
        String currentUserEmail = auth.getName();
        Member member = memberRepository.findByEmail(currentUserEmail).orElse(null);
        
        return member != null ? member.getId() : null;
    }
    
    /**
     * Gets current user's email
     */
    public String getCurrentUserEmail() {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        return auth != null ? auth.getName() : null;
    }
    
    /**
     * Checks if current user is a super user
     */
    public boolean isSuperUser(Authentication auth) {
        return auth.getAuthorities().stream()
                .anyMatch(authority -> authority.getAuthority().equals("ROLE_SUPER_USER"));
    }
    
    /**
     * Checks if current user is a super user
     */
    public boolean isCurrentUserSuperUser() {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        return auth != null && isSuperUser(auth);
    }
}