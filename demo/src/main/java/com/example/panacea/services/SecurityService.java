package com.example.panacea.services;

import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import java.util.regex.Pattern;
import java.util.Set;
import java.util.HashSet;
import java.util.List;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.concurrent.ConcurrentHashMap;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import java.time.LocalDateTime;
import java.time.Instant;
import java.util.Map;
import java.security.MessageDigest;
import java.nio.charset.StandardCharsets;
import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;
import java.util.Base64;

@Service
public class SecurityService {
    
    private static final Logger logger = LoggerFactory.getLogger(SecurityService.class);
    
    // Password policy constants
    private static final int MIN_PASSWORD_LENGTH = 8;
    private static final int MAX_FAILED_ATTEMPTS = 5;
    private static final int LOCKOUT_DURATION_MINUTES = 30;
    
    private static final Pattern PASSWORD_PATTERN = Pattern.compile(
        "^(?=.*[a-z])(?=.*[A-Z])(?=.*\\d)(?=.*[@$!%*?&])[A-Za-z\\d@$!%*?&]{8,}$"
    );
    
    // XSS prevention patterns
    private static final Pattern XSS_PATTERN = Pattern.compile(
        ".*[<>\"'&].*|.*script.*|.*javascript.*|.*vbscript.*|.*onload.*|.*onerror.*", 
        Pattern.CASE_INSENSITIVE
    );
    
    // Email validation
    private static final Pattern EMAIL_PATTERN = Pattern.compile(
        "^[a-zA-Z0-9_+&*-]+(?:\\.[a-zA-Z0-9_+&*-]+)*@(?:[a-zA-Z0-9-]+\\.)+[a-zA-Z]{2,7}$"
    );
    
    // Token blacklist (in production, use Redis or database)
    private final Set<String> blacklistedTokens = ConcurrentHashMap.newKeySet();
    
    // Failed login attempts tracking
    private final Map<String, FailedAttempt> failedAttempts = new ConcurrentHashMap<>();
    
    // Legacy field for compatibility with new methods
    private final Map<String, Integer> failedLoginAttempts = new ConcurrentHashMap<>();
    private final Map<String, LocalDateTime> accountLockoutTime = new ConcurrentHashMap<>();
    
    // Audit events tracking (in production, use database)
    private final List<String> auditEvents = new ArrayList<>();
    
    // Password history (in production, use database)
    private final Map<String, Set<String>> passwordHistory = new ConcurrentHashMap<>();
    
    @Value("${stripe.webhook.secret:}")
    private String stripeWebhookSecret;
    
    /**
     * Inner class to track failed login attempts
     */
    private static class FailedAttempt {
        private int count;
        private LocalDateTime lastAttempt;
        
        public FailedAttempt() {
            this.count = 1;
            this.lastAttempt = LocalDateTime.now();
        }
        
        public void increment() {
            this.count++;
            this.lastAttempt = LocalDateTime.now();
        }
        
        public boolean isLocked() {
            return count >= MAX_FAILED_ATTEMPTS && 
                   lastAttempt.isAfter(LocalDateTime.now().minusMinutes(LOCKOUT_DURATION_MINUTES));
        }
        
        public int getCount() { return count; }
        public LocalDateTime getLastAttempt() { return lastAttempt; }
    }
    
    /**
     * Validates password complexity requirements
     */
    public boolean isValidPassword(String password) {
        if (!StringUtils.hasText(password) || password.length() < MIN_PASSWORD_LENGTH) {
            return false;
        }
        return PASSWORD_PATTERN.matcher(password).matches();
    }
    
    /**
     * Validates email format
     */
    public boolean isValidEmail(String email) {
        if (!StringUtils.hasText(email)) {
            return false;
        }
        return EMAIL_PATTERN.matcher(email).matches();
    }
    
    /**
     * Sanitizes input to prevent XSS attacks
     */
    public String sanitizeInput(String input) {
        if (!StringUtils.hasText(input)) {
            return input;
        }
        
        return input
            .replace("<", "&lt;")
            .replace(">", "&gt;")
            .replace("\"", "&quot;")
            .replace("'", "&#x27;")
            .replace("&", "&amp;")
            .replace("/", "&#x2F;")
            .trim();
    }
    
    /**
     * Checks if input contains potential XSS patterns
     */
    public boolean containsXSS(String input) {
        if (!StringUtils.hasText(input)) {
            return false;
        }
        return XSS_PATTERN.matcher(input).find();
    }
    
    /**
     * Validates input for potential security threats
     */
    public void validateInput(String input, String fieldName) {
        if (containsXSS(input)) {
            logSecurityEvent("XSS_ATTEMPT", "system", 
                "Potential XSS detected in field: " + fieldName + " - Input: " + input);
            throw new SecurityException("Invalid input detected in " + fieldName);
        }
    }
    
    /**
     * Adds token to blacklist
     */
    public void blacklistToken(String token) {
        blacklistedTokens.add(token);
        logSecurityEvent("TOKEN_BLACKLISTED", "system", "JWT token blacklisted");
    }
    
    /**
     * Checks if token is blacklisted
     */
    public boolean isTokenBlacklisted(String token) {
        return blacklistedTokens.contains(token);
    }
    
    /**
     * Records a failed login attempt
     */
    public void recordFailedLoginAttempt(String email) {
        failedAttempts.compute(email, (key, attempt) -> {
            if (attempt == null) {
                return new FailedAttempt();
            } else {
                attempt.increment();
                return attempt;
            }
        });
        
        FailedAttempt attempt = failedAttempts.get(email);
        logSecurityEvent("FAILED_LOGIN_ATTEMPT", email, 
            "Failed login attempt #" + attempt.getCount());
        
        if (attempt.isLocked()) {
            logSecurityEvent("ACCOUNT_LOCKED", email, 
                "Account locked due to excessive failed login attempts");
        }
    }
    
    /**
     * Clears failed login attempts after successful login
     */
    public void clearFailedLoginAttempts(String email) {
        failedAttempts.remove(email);
        logSecurityEvent("SUCCESSFUL_LOGIN", email, "Failed login attempts cleared");
    }
    
    /**
     * Checks if account is locked due to failed login attempts
     */
    public boolean isAccountLocked(String email) {
        FailedAttempt attempt = failedAttempts.get(email);
        return attempt != null && attempt.isLocked();
    }
    
    /**
     * Validates user has permission to access resource
     */
    public boolean canAccessResource(String userEmail, String resourceOwnerEmail) {
        if (!StringUtils.hasText(userEmail) || !StringUtils.hasText(resourceOwnerEmail)) {
            return false;
        }
        boolean canAccess = userEmail.equals(resourceOwnerEmail);
        
        if (!canAccess) {
            logSecurityEvent("UNAUTHORIZED_ACCESS_ATTEMPT", userEmail, 
                "Attempted to access resource owned by: " + resourceOwnerEmail);
        }
        
        return canAccess;
    }
    
    /**
     * Validates password against history to prevent reuse
     */
    public boolean isPasswordReused(String email, String newPassword) {
        Set<String> history = passwordHistory.get(email);
        if (history == null) {
            return false;
        }
        
        String hashedPassword = hashPassword(newPassword);
        return history.contains(hashedPassword);
    }
    
    /**
     * Adds password to history
     */
    public void addPasswordToHistory(String email, String password) {
        String hashedPassword = hashPassword(password);
        passwordHistory.computeIfAbsent(email, k -> new HashSet<>()).add(hashedPassword);
        
        // Keep only last 5 passwords in history
        Set<String> history = passwordHistory.get(email);
        if (history.size() > 5) {
            // Remove oldest password (simplified - in production use proper ordering)
            history.iterator().next();
            history.remove(history.iterator().next());
        }
    }
    
    /**
     * Verifies Stripe webhook signature
     */
    public boolean verifyStripeWebhookSignature(String payload, String signature) {
        if (!StringUtils.hasText(stripeWebhookSecret) || !StringUtils.hasText(signature)) {
            return false;
        }
        
        try {
            String expectedSignature = computeStripeSignature(payload);
            boolean isValid = signature.equals(expectedSignature);
            
            if (!isValid) {
                logSecurityEvent("STRIPE_WEBHOOK_INVALID_SIGNATURE", "system", 
                    "Invalid Stripe webhook signature received");
            }
            
            return isValid;
        } catch (Exception e) {
            logger.error("Error verifying Stripe webhook signature", e);
            return false;
        }
    }
    
    /**
     * Logs security events for audit purposes
     */
    public void logSecurityEvent(String event, String userEmail, String details) {
        logger.warn("SECURITY EVENT: {} - User: {} - Details: {} - Timestamp: {}", 
            event, userEmail, details, LocalDateTime.now());
    }
    
    /**
     * Hashes password for storage/comparison
     */
    private String hashPassword(String password) {
        try {
            MessageDigest digest = MessageDigest.getInstance("SHA-256");
            byte[] hash = digest.digest(password.getBytes(StandardCharsets.UTF_8));
            return Base64.getEncoder().encodeToString(hash);
        } catch (Exception e) {
            logger.error("Error hashing password", e);
            throw new RuntimeException("Password hashing failed");
        }
    }
    
    /**
     * Computes Stripe webhook signature
     */
    private String computeStripeSignature(String payload) throws Exception {
        Mac mac = Mac.getInstance("HmacSHA256");
        SecretKeySpec secretKeySpec = new SecretKeySpec(
            stripeWebhookSecret.getBytes(StandardCharsets.UTF_8), "HmacSHA256");
        mac.init(secretKeySpec);
        
        byte[] hash = mac.doFinal(payload.getBytes(StandardCharsets.UTF_8));
        return "sha256=" + Base64.getEncoder().encodeToString(hash);
    }
    
    /**
     * Get password policy requirements for display
     */
    public String getPasswordPolicyMessage() {
        return "Password must be at least " + MIN_PASSWORD_LENGTH + " characters long and contain: " +
               "uppercase letter, lowercase letter, number, and special character (@$!%*?&)";
    }
    
    /**
     * Gets current user's email from security context
     */
    public String getCurrentUserEmail() {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        return auth != null ? auth.getName() : "system";
    }
    
    /**
     * Gets audit logs for security monitoring
     */
    public List<Map<String, Object>> getAuditLogs(int limit) {
        // This would typically query a database table
        // For now, returning the in-memory audit events
        List<Map<String, Object>> auditLogs = new ArrayList<>();
        
        int count = 0;
        for (String event : auditEvents) {
            if (count >= limit) break;
            
            Map<String, Object> logEntry = new HashMap<>();
            logEntry.put("timestamp", Instant.now().toString());
            logEntry.put("event", event);
            auditLogs.add(logEntry);
            count++;
        }
        
        return auditLogs;
    }
    
    /**
     * Gets failed login attempts for security monitoring
     */
    public List<Map<String, Object>> getFailedLoginAttempts(int limit) {
        List<Map<String, Object>> failedAttempts = new ArrayList<>();
        
        int count = 0;
        for (Map.Entry<String, Integer> entry : failedLoginAttempts.entrySet()) {
            if (count >= limit) break;
            
            Map<String, Object> attempt = new HashMap<>();
            attempt.put("email", entry.getKey());
            attempt.put("attempts", entry.getValue());
            attempt.put("locked", isAccountLocked(entry.getKey()));
            failedAttempts.add(attempt);
            count++;
        }
        
        return failedAttempts;
    }
    
    /**
     * Gets list of blacklisted tokens
     */
    public List<String> getBlacklistedTokens() {
        return new ArrayList<>(blacklistedTokens);
    }
    
    /**
     * Records a failed login attempt
     */
    public void recordFailedAttempt(String email) {
        failedAttempts.compute(email, (key, attempt) -> {
            if (attempt == null) {
                return new FailedAttempt();
            } else {
                attempt.increment();
                return attempt;
            }
        });
        
        // Also update the legacy tracking
        failedLoginAttempts.merge(email, 1, Integer::sum);
        
        logSecurityEvent("FAILED_LOGIN_ATTEMPT", email, 
            "Failed login attempt #" + failedAttempts.get(email).getCount());
    }
    
    /**
     * Resets failed login attempts after successful authentication
     */
    public void resetFailedAttempts(String email) {
        failedAttempts.remove(email);
        failedLoginAttempts.remove(email);
        accountLockoutTime.remove(email);
        
        logSecurityEvent("FAILED_ATTEMPTS_RESET", email, "Failed attempts reset after successful login");
    }
    
    /**
     * Checks if a password was recently used
     */
    public boolean isPasswordInHistory(String email, String newPassword) {
        Set<String> history = passwordHistory.get(email);
        if (history == null) {
            return false;
        }
        
        String hashedNewPassword = hashPassword(newPassword);
        return history.contains(hashedNewPassword);
    }
    
    /**
     * Adds password to history after successful change
     */
    public void addToPasswordHistory(String email, String hashedPassword) {
        passwordHistory.computeIfAbsent(email, k -> new HashSet<>()).add(hashedPassword);
        
        // Keep only last 5 passwords
        Set<String> history = passwordHistory.get(email);
        if (history.size() > 5) {
            // Remove oldest - in production, you'd want to track timestamps
            String oldest = history.iterator().next();
            history.remove(oldest);
        }
        
        logSecurityEvent("PASSWORD_HISTORY_UPDATED", email, "Password added to history");
    }
    
    /**
     * Unlocks a user account
     */
    public void unlockAccount(String email) {
        failedLoginAttempts.remove(email);
        accountLockoutTime.remove(email);
        failedAttempts.remove(email);
        logSecurityEvent("ACCOUNT_UNLOCKED", email, "Account manually unlocked by admin");
    }
}