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
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class PasswordService {

    private final MemberRepository memberRepository;
    private final PasswordResetTokenRepository tokenRepository;
    private final RevokedTokenRepository revokedRepository;
    private final PasswordEncoder passwordEncoder;
    private final JavaMailSender mailSender;

    @Value("${app.frontend.reset-password-url}")
    private String resetPasswordUrl;

    @Transactional
    public void forgotPassword(ForgotPasswordRequest request) {
        Member member = memberRepository.findByEmail(request.getEmail())
                .orElseThrow(() -> new MemberNotFoundException("No member with email: " + request.getEmail()));

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
        mailSender.send(message);
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

        Member member = prt.getMember();
        member.setPassword(passwordEncoder.encode(request.getNewPassword()));
        memberRepository.save(member);

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
