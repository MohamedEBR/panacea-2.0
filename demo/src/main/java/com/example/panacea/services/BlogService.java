package com.example.panacea.services;

import com.example.panacea.dto.BlogResponse;
import com.example.panacea.dto.CreateBlogRequest;
import com.example.panacea.dto.UpdateBlogRequest;
import com.example.panacea.models.Blog;
import com.example.panacea.models.Member;
import com.example.panacea.repo.BlogRepository;
import com.example.panacea.repo.MemberRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional
public class BlogService {
    
    private final BlogRepository blogRepository;
    private final MemberRepository memberRepository;
    private final SecurityService securityService;
    
    /**
     * Get all published blogs for public viewing
     */
    public List<BlogResponse> getPublishedBlogs() {
        List<Blog> blogs = blogRepository.findByPublishedTrueOrderByCreatedAtDesc();
        return blogs.stream()
                .map(this::mapToBlogResponse)
                .collect(Collectors.toList());
    }
    
    /**
     * Get published blogs with pagination
     */
    public Page<BlogResponse> getPublishedBlogs(Pageable pageable) {
        Page<Blog> blogs = blogRepository.findByPublishedTrueOrderByCreatedAtDesc(pageable);
        return blogs.map(this::mapToBlogResponse);
    }
    
    /**
     * Get all blogs (admin only)
     */
    public Page<BlogResponse> getAllBlogs(Pageable pageable) {
        Page<Blog> blogs = blogRepository.findAllByOrderByCreatedAtDesc(pageable);
        return blogs.map(this::mapToBlogResponse);
    }
    
    /**
     * Get a single published blog by ID
     */
    public BlogResponse getPublishedBlog(Long id) {
        Blog blog = blogRepository.findByIdAndPublishedTrue(id)
                .orElseThrow(() -> new RuntimeException("Blog not found or not published"));
        return mapToBlogResponse(blog);
    }
    
    /**
     * Get any blog by ID (admin only)
     */
    public BlogResponse getBlog(Long id) {
        Blog blog = blogRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Blog not found"));
        return mapToBlogResponse(blog);
    }
    
    /**
     * Create a new blog (admin only)
     */
    public BlogResponse createBlog(CreateBlogRequest request, Authentication authentication) {
        // Validate input for XSS
        if (!securityService.sanitizeInput(request.getTitle()).equals(request.getTitle()) ||
            !securityService.sanitizeInput(request.getContent()).equals(request.getContent())) {
            securityService.logSecurityEvent("XSS_ATTEMPT_BLOG_CREATE", 
                authentication.getName(), "Attempted XSS in blog creation");
            throw new RuntimeException("Invalid input detected");
        }
        
        Member author = memberRepository.findByEmail(authentication.getName())
                .orElseThrow(() -> new RuntimeException("Author not found"));
        
        Blog blog = Blog.builder()
                .title(request.getTitle())
                .content(request.getContent())
                .imageUrl(request.getImageUrl())
                .blogDate(request.getBlogDate())
                .published(request.getPublished())
                .author(author)
                .build();
        
        Blog savedBlog = blogRepository.save(blog);
        securityService.logSecurityEvent("BLOG_CREATED", authentication.getName(), 
            "Created blog: " + savedBlog.getTitle());
        
        return mapToBlogResponse(savedBlog);
    }
    
    /**
     * Update a blog (admin only)
     */
    public BlogResponse updateBlog(Long id, UpdateBlogRequest request, Authentication authentication) {
        // Validate input for XSS
        if (request.getTitle() != null && !securityService.sanitizeInput(request.getTitle()).equals(request.getTitle()) ||
            request.getContent() != null && !securityService.sanitizeInput(request.getContent()).equals(request.getContent())) {
            securityService.logSecurityEvent("XSS_ATTEMPT_BLOG_UPDATE", 
                authentication.getName(), "Attempted XSS in blog update");
            throw new RuntimeException("Invalid input detected");
        }
        
        Blog blog = blogRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Blog not found"));
        
        // Update fields only if they are provided
        if (request.getTitle() != null) {
            blog.setTitle(request.getTitle());
        }
        if (request.getContent() != null) {
            blog.setContent(request.getContent());
        }
        if (request.getImageUrl() != null) {
            blog.setImageUrl(request.getImageUrl());
        }
        if (request.getBlogDate() != null) {
            blog.setBlogDate(request.getBlogDate());
        }
        if (request.getPublished() != null) {
            blog.setPublished(request.getPublished());
        }
        
        Blog updatedBlog = blogRepository.save(blog);
        securityService.logSecurityEvent("BLOG_UPDATED", authentication.getName(), 
            "Updated blog: " + updatedBlog.getTitle());
        
        return mapToBlogResponse(updatedBlog);
    }
    
    /**
     * Delete a blog (admin only)
     */
    public void deleteBlog(Long id, Authentication authentication) {
        Blog blog = blogRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Blog not found"));
        
        String blogTitle = blog.getTitle();
        blogRepository.delete(blog);
        
        securityService.logSecurityEvent("BLOG_DELETED", authentication.getName(), 
            "Deleted blog: " + blogTitle);
    }
    
    /**
     * Search published blogs
     */
    public List<BlogResponse> searchBlogs(String keyword) {
        List<Blog> blogs = blogRepository.searchPublishedBlogs(keyword);
        return blogs.stream()
                .map(this::mapToBlogResponse)
                .collect(Collectors.toList());
    }
    
    /**
     * Get blog statistics
     */
    public BlogStats getBlogStats() {
        long publishedCount = blogRepository.countByPublishedTrue();
        long totalCount = blogRepository.count();
        
        return BlogStats.builder()
                .publishedCount(publishedCount)
                .totalCount(totalCount)
                .draftCount(totalCount - publishedCount)
                .build();
    }
    
    private BlogResponse mapToBlogResponse(Blog blog) {
        return BlogResponse.builder()
                .id(blog.getId())
                .title(blog.getTitle())
                .content(blog.getContent())
                .imageUrl(blog.getImageUrl())
                .blogDate(blog.getBlogDate())
                .published(blog.getPublished())
                .createdAt(blog.getCreatedAt())
                .updatedAt(blog.getUpdatedAt())
                .authorName(blog.getAuthor() != null ? 
                    blog.getAuthor().getName() + " " + blog.getAuthor().getLastName() : "Unknown")
                .build();
    }
    
    @lombok.Builder
    @lombok.Data
    public static class BlogStats {
        private long totalCount;
        private long publishedCount;
        private long draftCount;
    }
}