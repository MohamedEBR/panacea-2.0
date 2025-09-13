package com.example.panacea.config;

import com.example.panacea.enums.MemberStatus;
import com.example.panacea.enums.Role;
import com.example.panacea.models.Member;
import com.example.panacea.repo.MemberRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.Optional;

@Configuration
public class AdminInitializer {

    private static final Logger log = LoggerFactory.getLogger(AdminInitializer.class);

    @Value("${app.admin.email:admin@panacea.local}")
    private String adminEmail;

    @Value("${app.admin.password:ChangeMe!123}")
    private String adminPassword;

    @Bean
    CommandLineRunner seedAdmin(MemberRepository memberRepository, PasswordEncoder passwordEncoder) {
        return args -> {
            if (args != null && args.length > 0) {
                log.debug("AdminInitializer received {} args", args.length);
            }
            String email = adminEmail.trim().toLowerCase();
            Optional<Member> existing = memberRepository.findByEmailIgnoreCase(email);
            if (existing.isPresent()) {
                return;
            }

            Member admin = Member.builder()
                    .name("Admin")
                    .lastName("User")
                    .email(email)
                    .password(passwordEncoder.encode(adminPassword))
                    .role(Role.SUPER_USER)
                    .status(MemberStatus.ACTIVE)
                    .build();

            memberRepository.save(admin);
            log.warn("Seeded SUPER_USER admin account: {} (please change the password via API immediately)", email);
        };
    }
}
