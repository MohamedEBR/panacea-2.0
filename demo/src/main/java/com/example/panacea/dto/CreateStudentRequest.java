package com.example.panacea.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CreateStudentRequest {

    @NotBlank(message = "Name is required")
    private String name;

    @NotNull(message = "DOB is required")
    private LocalDate dob;

    @NotNull(message = "Weight is required")
    private Integer weight;

    @NotNull(message = "Height is required")
    private Integer height;

    private String medicalConcerns; // optional

    // Optional fields; if provided will be mapped to enums
    private String gender;
    private String belt;
}
