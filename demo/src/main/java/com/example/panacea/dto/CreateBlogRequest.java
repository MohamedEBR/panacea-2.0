package com.example.panacea.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CreateBlogRequest {
    
    @NotBlank(message = "Title is required")
    @Size(max = 500, message = "Title cannot exceed 500 characters")
    private String title;
    
    @NotBlank(message = "Content is required")
    private String content;
    
    private String imageUrl;
    
    private String blogDate; // Display date for the blog
    
    @Builder.Default
    private Boolean published = false;
}