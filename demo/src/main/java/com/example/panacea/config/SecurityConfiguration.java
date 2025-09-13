package com.example.panacea.config;

import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.security.web.header.writers.ContentSecurityPolicyHeaderWriter;
import org.springframework.security.web.header.writers.frameoptions.XFrameOptionsHeaderWriter;
import org.springframework.security.web.header.writers.StaticHeadersWriter;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Configuration
@EnableWebSecurity
@EnableMethodSecurity(prePostEnabled = true)
@RequiredArgsConstructor
public class SecurityConfiguration {

    private final JwtAuthenticationFilter jwtAuthFilter;
    private final AuthenticationProvider authenticationProvider;

    private static final Logger logger = LoggerFactory.getLogger(SecurityConfiguration.class);

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        logger.info("Configuring security headers...");
        http
                .csrf(csrf -> csrf.disable())
                .headers(headers -> {
                    // Configure headers - disable XSS protection first, then add our custom one
                    headers.xssProtection(xss -> xss.disable());
                    headers.addHeaderWriter(new ContentSecurityPolicyHeaderWriter("default-src 'self'; script-src 'self'; object-src 'none'; style-src 'self' 'unsafe-inline'; font-src 'self'; img-src 'self' data:; frame-ancestors 'none';"));
                    headers.addHeaderWriter(new XFrameOptionsHeaderWriter(XFrameOptionsHeaderWriter.XFrameOptionsMode.DENY));
                    headers.addHeaderWriter(new StaticHeadersWriter("Strict-Transport-Security", "max-age=31536000; includeSubDomains"));
                    headers.addHeaderWriter(new StaticHeadersWriter("X-Content-Type-Options", "nosniff"));
                    headers.addHeaderWriter(new StaticHeadersWriter("X-XSS-Protection", "1; mode=block"));
                    headers.addHeaderWriter(new StaticHeadersWriter("Referrer-Policy", "strict-origin-when-cross-origin"));
                    headers.addHeaderWriter(new StaticHeadersWriter("Permissions-Policy", "geolocation=(), microphone=(), camera=(), payment=(), usb=()"));
                    logger.info("Security headers configured: CSP, X-Frame-Options, HSTS, X-Content-Type-Options, X-XSS-Protection, Referrer-Policy, Permissions-Policy");
                })
                .authorizeHttpRequests(auth -> auth
                        // Public auth endpoints
                        .requestMatchers("/api/v1/auth/**").permitAll()
                        // Stripe webhook endpoint (needs signature verification instead of JWT)
                        .requestMatchers("/api/stripe/webhook").permitAll()
                        // Blog endpoints - public read access, admin write access
                        .requestMatchers("/api/blogs", "/api/blogs/*", "/api/blogs/search").permitAll()
                        .requestMatchers("/api/blogs/admin/**").hasRole("SUPER_USER")
                        // Admin-only endpoints
                        .requestMatchers("/api/v1/admin/**").hasRole("SUPER_USER")
                        // Member endpoints - users can only access their own data
                        .requestMatchers("/api/v1/members/**").hasAnyRole("USER", "SUPER_USER")
                        // Student endpoints - users can only access their own students
                        .requestMatchers("/api/v1/students/**").hasAnyRole("USER", "SUPER_USER")
                        // Stripe payment endpoints for authenticated users
                        .requestMatchers("/api/stripe/**").hasAnyRole("USER", "SUPER_USER")
                        // Demo controller for testing
                        .requestMatchers("/api/v1/demo-controller").authenticated()
                        // Everything else requires authentication
                        .anyRequest().authenticated()
                )
                .sessionManagement(session -> session
                        .sessionCreationPolicy(SessionCreationPolicy.STATELESS)
                )
                .authenticationProvider(authenticationProvider)
                .addFilterBefore(jwtAuthFilter, UsernamePasswordAuthenticationFilter.class);

        return http.build();
    }
}
