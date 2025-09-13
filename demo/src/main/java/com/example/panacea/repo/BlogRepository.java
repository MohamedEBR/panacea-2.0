package com.example.panacea.repo;

import com.example.panacea.models.Blog;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface BlogRepository extends JpaRepository<Blog, Long> {
    
    // Find all published blogs ordered by creation date (newest first)
    List<Blog> findByPublishedTrueOrderByCreatedAtDesc();
    
    // Find all published blogs with pagination
    Page<Blog> findByPublishedTrueOrderByCreatedAtDesc(Pageable pageable);
    
    // Find all blogs (for admin) with pagination
    Page<Blog> findAllByOrderByCreatedAtDesc(Pageable pageable);
    
    // Find blog by ID and published status
    Optional<Blog> findByIdAndPublishedTrue(Long id);
    
    // Search published blogs by title or content
    @Query("SELECT b FROM Blog b WHERE b.published = true AND " +
           "(LOWER(b.title) LIKE LOWER(CONCAT('%', :keyword, '%')) OR " +
           "LOWER(b.content) LIKE LOWER(CONCAT('%', :keyword, '%')))")
    List<Blog> searchPublishedBlogs(String keyword);
    
    // Count published blogs
    long countByPublishedTrue();
}