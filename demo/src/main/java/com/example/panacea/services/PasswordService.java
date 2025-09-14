package com.example.panacea.services;

import com.example.panacea.dto.ForgotPasswordRequest;
import com.example.panacea.dto.ResetPasswordRequest;
import com.example.panacea.exceptions.InvalidTokenException;
import com.example.panacea.exceptions.MemberNotFoundException;
import com.example.panacea.exceptions.PasswordMismatchException;
import com.example.panacea.models.Member;
import com.example.panacea.models.PasswordResetToken;
import com.example.panacea.models.RevokedToken;
import com.example.panacea.repo.MemberRepository;
import com.example.panacea.repo.PasswordResetTokenRepository;
import com.example.panacea.repo.RevokedTokenRepository;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.mail.MailException;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.UUID;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Service
@RequiredArgsConstructor
public class PasswordService {
    private static final Logger logger = LoggerFactory.getLogger(PasswordService.class);

    private final MemberRepository memberRepository;
    private final PasswordResetTokenRepository tokenRepository;
    private final RevokedTokenRepository revokedRepository;
    private final JavaMailSender mailSender;
    private final PasswordEncoder passwordEncoder;
    private final SecurityService securityService;

    @Value("${app.frontend.reset-password-url}")
    private String resetPasswordUrl;

    @Transactional(noRollbackFor = MailException.class)
    public void forgotPassword(ForgotPasswordRequest request) {
        String email = request.getEmail() == null ? null : request.getEmail().trim();
        Member member = memberRepository.findByEmailIgnoreCase(email)
                .orElseThrow(() -> new MemberNotFoundException("No member with email: " + email));

        // Remove any existing tokens for this member
        tokenRepository.deleteByMemberId(member.getId());

        // Generate new token
        String token = UUID.randomUUID().toString();
        LocalDateTime expiry = LocalDateTime.now().plusHours(2);
        PasswordResetToken prt = PasswordResetToken.builder()
                .token(token)
                .member(member)
                .expiryDate(expiry)
                .build();
        tokenRepository.save(prt);

        String link = resetPasswordUrl + "?token=" + token;
        SimpleMailMessage message = new SimpleMailMessage();
        message.setTo(member.getEmail());
        message.setSubject("Panacea Karate Academy - Password Reset");
        message.setText(
                "Hello " + member.getName() + ",\n\n" +
                        "We received a request to reset your password. Click the link below to set a new password (valid for 2 hours):\n" +
                        link + "\n\n" +
                        "If you didn't request this, please ignore this email.\n\n" +
                        "Thanks,\nPanacea Karate Academy Team"
        );
        try {
            mailSender.send(message);
        } catch (Exception e) {
            // Do not fail the request if email sending has issues (e.g., SMTP misconfig)
            securityService.logSecurityEvent("EMAIL_SEND_FAILURE", member.getEmail(),
                    "Forgot-password email could not be sent: " + e.getMessage());
            // Developer aid: log the link so it can be used during local testing
            logger.info("[DEV ONLY] Password reset link for {}: {}", member.getEmail(), link);
        }
    }

    @Transactional
    public void resetPassword(ResetPasswordRequest request) {
        PasswordResetToken prt = tokenRepository.findByToken(request.getToken())
                .orElseThrow(() -> new InvalidTokenException("Invalid or expired reset token"));

        if (prt.getExpiryDate().isBefore(LocalDateTime.now())) {
            throw new InvalidTokenException("Reset token has expired");
        }

        if (!request.getNewPassword().equals(request.getConfirmPassword())) {
            throw new PasswordMismatchException("Passwords do not match");
        }

        // Validate password using SecurityService
        if (!securityService.isValidPassword(request.getNewPassword())) {
            throw new IllegalArgumentException(securityService.getPasswordPolicyMessage());
        }

        Member member = prt.getMember();
        
        // Check password history
        if (securityService.isPasswordInHistory(member.getEmail(), request.getNewPassword())) {
            throw new IllegalArgumentException("Password was recently used. Please choose a different password.");
        }
        
        String encodedPassword = passwordEncoder.encode(request.getNewPassword());
        member.setPassword(encodedPassword);
        memberRepository.save(member);

        // Update password history
        securityService.addToPasswordHistory(member.getEmail(), encodedPassword);
        
        // Log security event
        securityService.logSecurityEvent("PASSWORD_RESET", member.getEmail(), 
            "Password successfully reset using token");

        // Clean up token
        tokenRepository.delete(prt);
    }

    @Transactional
    public void logout(String token) {
        // Revoke the token by storing it until expiration
        RevokedToken rt = RevokedToken.builder()
                .token(token)
                .expiryDate(LocalDateTime.now().plusHours(1))
                .build();
        revokedRepository.save(rt);
    }
}
