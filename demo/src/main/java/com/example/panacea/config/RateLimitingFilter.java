package com.example.panacea.config;

import jakarta.servlet.Filter;
import jakarta.servlet.FilterChain;
import jakarta.servlet.FilterConfig;
import jakarta.servlet.ServletException;
import jakarta.servlet.ServletRequest;
import jakarta.servlet.ServletResponse;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.TimeUnit;

@Component
public class RateLimitingFilter implements Filter {

    private final Map<String, RateLimiter> limiters = new ConcurrentHashMap<>();

    @Override
    public void init(FilterConfig filterConfig) throws ServletException {
        // Initialization logic if needed
    }

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {
        if (request instanceof HttpServletRequest) {
            HttpServletRequest httpRequest = (HttpServletRequest) request;
            HttpServletResponse httpResponse = (HttpServletResponse) response;
            String requestURI = httpRequest.getRequestURI();
            
            // Only apply rate limiting to authentication endpoints
            if (requestURI.startsWith("/api/v1/auth/")) {
                String clientIp = httpRequest.getRemoteAddr();

                RateLimiter rateLimiter = limiters.computeIfAbsent(clientIp, ip -> {
                    // Explicitly use the lambda parameter to resolve the lint error
                    System.out.println("Creating RateLimiter for IP: " + ip);
                    return new RateLimiter(10, TimeUnit.MINUTES); // Allow 10 requests per minute for testing
                });

                if (!rateLimiter.allowRequest()) {
                    httpResponse.setStatus(429); // Too Many Requests
                    httpResponse.setContentType("text/plain");
                    httpResponse.setCharacterEncoding("UTF-8");
                    httpResponse.getWriter().write("Too many requests. Please try again later.");
                    return;
                }
            }
        }

        chain.doFilter(request, response);
    }

    @Override
    public void destroy() {
        // Cleanup logic if needed
    }

    private static class RateLimiter {
        private final long maxRequests;
        private final long timeWindowMillis;
        private long requestCount;
        private long windowStart;

        public RateLimiter(long maxRequests, TimeUnit timeWindow) {
            this.maxRequests = maxRequests;
            this.timeWindowMillis = timeWindow.toMillis(1);
            this.requestCount = 0;
            this.windowStart = System.currentTimeMillis();
        }

        public synchronized boolean allowRequest() {
            long now = System.currentTimeMillis();

            if (now - windowStart > timeWindowMillis) {
                windowStart = now;
                requestCount = 0;
            }

            if (requestCount < maxRequests) {
                requestCount++;
                return true;
            }

            return false;
        }
    }
}