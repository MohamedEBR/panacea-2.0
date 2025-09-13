package com.example.panacea.controllers;

import com.example.panacea.dto.BlogResponse;
import com.example.panacea.dto.CreateBlogRequest;
import com.example.panacea.dto.UpdateBlogRequest;
import com.example.panacea.services.BlogService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/v1/blogs")
@RequiredArgsConstructor
@CrossOrigin(origins = {"http://localhost:3000", "http://localhost:5173"}) // For React dev server
public class BlogController {
    
    private final BlogService blogService;
    
    /**
     * Get all published blogs - accessible by everyone
     */
    @GetMapping
    public ResponseEntity<Map<String, Object>> getPublishedBlogs(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {
        try {
            if (size == 0) {
                // Return all blogs without pagination
                List<BlogResponse> blogs = blogService.getPublishedBlogs();
                return ResponseEntity.ok(Map.of(
                    "success", true,
                    "data", Map.of(
                        "blogs", blogs,
                        "totalElements", blogs.size()
                    )
                ));
            } else {
                // Return paginated results
                Pageable pageable = PageRequest.of(page, size);
                Page<BlogResponse> blogPage = blogService.getPublishedBlogs(pageable);
                return ResponseEntity.ok(Map.of(
                    "success", true,
                    "data", Map.of(
                        "blogs", blogPage.getContent(),
                        "currentPage", page,
                        "totalPages", blogPage.getTotalPages(),
                        "totalElements", blogPage.getTotalElements()
                    )
                ));
            }
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(Map.of(
                "success", false,
                "message", "Failed to fetch blogs: " + e.getMessage()
            ));
        }
    }
    
    /**
     * Get all blogs including unpublished ones - admin only
     */
    @GetMapping("/admin/all")
    @PreAuthorize("hasRole('SUPER_USER')")
    public ResponseEntity<Map<String, Object>> getAllBlogs(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {
        try {
            Pageable pageable = PageRequest.of(page, size);
            Page<BlogResponse> blogPage = blogService.getAllBlogs(pageable);
            return ResponseEntity.ok(Map.of(
                "success", true,
                "data", Map.of(
                    "blogs", blogPage.getContent(),
                    "currentPage", page,
                    "totalPages", blogPage.getTotalPages(),
                    "totalElements", blogPage.getTotalElements()
                )
            ));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(Map.of(
                "success", false,
                "message", "Failed to fetch blogs: " + e.getMessage()
            ));
        }
    }
    
    /**
     * Get a single published blog by ID - accessible by everyone
     */
    @GetMapping("/{id}")
    public ResponseEntity<Map<String, Object>> getBlog(@PathVariable Long id) {
        try {
            BlogResponse blog = blogService.getPublishedBlog(id);
            return ResponseEntity.ok(Map.of(
                "success", true,
                "data", Map.of("blog", blog)
            ));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(Map.of(
                "success", false,
                "message", "Blog not found"
            ));
        }
    }
    
    /**
     * Get any blog by ID (including unpublished) - admin only
     */
    @GetMapping("/admin/{id}")
    @PreAuthorize("hasRole('SUPER_USER')")
    public ResponseEntity<Map<String, Object>> getBlogAdmin(@PathVariable Long id) {
        try {
            BlogResponse blog = blogService.getBlog(id);
            return ResponseEntity.ok(Map.of(
                "success", true,
                "data", Map.of("blog", blog)
            ));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(Map.of(
                "success", false,
                "message", "Blog not found"
            ));
        }
    }
    
    /**
     * Create a new blog - admin only
     */
    @PostMapping
    @PreAuthorize("hasRole('SUPER_USER')")
    public ResponseEntity<Map<String, Object>> createBlog(
            @Valid @RequestBody CreateBlogRequest request,
            Authentication authentication) {
        try {
            BlogResponse blog = blogService.createBlog(request, authentication);
            return ResponseEntity.status(HttpStatus.CREATED).body(Map.of(
                "success", true,
                "message", "Blog created successfully",
                "data", Map.of("blog", blog)
            ));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(Map.of(
                "success", false,
                "message", "Failed to create blog: " + e.getMessage()
            ));
        }
    }
    
    /**
     * Update a blog - admin only
     */
    @PutMapping("/{id}")
    @PreAuthorize("hasRole('SUPER_USER')")
    public ResponseEntity<Map<String, Object>> updateBlog(
            @PathVariable Long id,
            @Valid @RequestBody UpdateBlogRequest request,
            Authentication authentication) {
        try {
            BlogResponse blog = blogService.updateBlog(id, request, authentication);
            return ResponseEntity.ok(Map.of(
                "success", true,
                "message", "Blog updated successfully",
                "data", Map.of("blog", blog)
            ));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(Map.of(
                "success", false,
                "message", "Failed to update blog: " + e.getMessage()
            ));
        }
    }
    
    /**
     * Delete a blog - admin only
     */
    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('SUPER_USER')")
    public ResponseEntity<Map<String, Object>> deleteBlog(
            @PathVariable Long id,
            Authentication authentication) {
        try {
            blogService.deleteBlog(id, authentication);
            return ResponseEntity.ok(Map.of(
                "success", true,
                "message", "Blog deleted successfully"
            ));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(Map.of(
                "success", false,
                "message", "Failed to delete blog: " + e.getMessage()
            ));
        }
    }
    
    /**
     * Search published blogs - accessible by everyone
     */
    @GetMapping("/search")
    public ResponseEntity<Map<String, Object>> searchBlogs(@RequestParam String q) {
        try {
            List<BlogResponse> blogs = blogService.searchBlogs(q);
            return ResponseEntity.ok(Map.of(
                "success", true,
                "data", Map.of(
                    "blogs", blogs,
                    "totalElements", blogs.size(),
                    "query", q
                )
            ));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(Map.of(
                "success", false,
                "message", "Failed to search blogs: " + e.getMessage()
            ));
        }
    }
    
    /**
     * Get blog statistics - admin only
     */
    @GetMapping("/admin/stats")
    @PreAuthorize("hasRole('SUPER_USER')")
    public ResponseEntity<Map<String, Object>> getBlogStats() {
        try {
            BlogService.BlogStats stats = blogService.getBlogStats();
            return ResponseEntity.ok(Map.of(
                "success", true,
                "data", stats
            ));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(Map.of(
                "success", false,
                "message", "Failed to fetch stats: " + e.getMessage()
            ));
        }
    }
}