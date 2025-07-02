package com.example.panacea.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UpdateStudentInfoRequest {

    @NotBlank(message = "Name is Required")
    private String name;

    @NotEmpty(message = "DOB is required")
    private LocalDate dob;

    @NotBlank(message = "weight is required")
    private int weight;

    @NotBlank(message = "height is required")
    private int height;

    private String medicalConcerns;



}
