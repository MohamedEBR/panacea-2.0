package com.example.panacea.dto;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UpdateStudentsRequest {

    @NotEmpty(message = "At least one student is required")
    @Valid
    private ArrayList<StudentDto> students;

    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class StudentDto {

        @NotBlank(message = "Student name is required")
        private String name;

        @NotNull(message = "Date of birth is required")
        private LocalDate dob;

        @NotNull(message = "Weight is required")
        private Integer weight;

        @NotNull(message = "Height is required")
        private Integer height;

        private String medicalConcerns;  // Optional

        @NotBlank(message = "Gender is required")
        private String gender;

        @NotBlank(message = "Belt is required")
        private String belt;

        @NotEmpty(message = "At least one program ID is required")
        private List<Long> programIds;

        @NotNull(message = "Years in club is required")
        private Integer yearsInClub;
    }
}
