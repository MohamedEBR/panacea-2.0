package com.example.panacea.dto;

import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UpdateBlogRequest {
    
    @Size(max = 500, message = "Title cannot exceed 500 characters")
    private String title;
    
    private String content;
    
    private String imageUrl;
    
    private String blogDate;
    
    private Boolean published;
}