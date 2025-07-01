package com.example.panacea.dto;

import jakarta.validation.constraints.*;
import lombok.*;

import java.time.LocalDate;
import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class RegisterRequest {

    @NotBlank(message = "First name is required")
    private String name;

    @NotBlank(message = "Last name is required")
    private String lastName;

    @Email(message = "Invalid email format")
    @NotBlank(message = "Email is required")
    private String email;

    @NotBlank(message = "Password is required")
    @Size(min = 8, message = "Password must be at least 8 characters")
    @Pattern(
            regexp = "^(?=.*[A-Z])(?=.*\\d)(?=.*[@$!%*?&])[A-Za-z\\d@$!%*?&]{8,}$",
            message = "Password must have at least one uppercase letter, one number, and one special character"
    )
    private String password;

    @NotNull(message = "Birth date is required")
    @Past(message = "Birth date must be in the past")
    private LocalDate birthDate;

    @NotBlank(message = "Phone number is required")
    @Pattern(regexp = "^[0-9\\-\\+]{9,15}$", message = "Invalid phone number format")
    private String phone;

    @NotBlank(message = "Address is required")
    private String address;

    @NotBlank(message = "City is required")
    private String city;

    @NotBlank(message = "Postal code is required")
    @Pattern(regexp = "^[A-Za-z]\\d[A-Za-z][ -]?\\d[A-Za-z]\\d$", message = "Invalid Canadian postal code format")
    private String postalCode;

    @NotEmpty(message = "At least one student is required")
    private List<StudentRequest> students;

    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class StudentRequest {

        @NotBlank(message = "Student name is required")
        private String name;

        @NotNull(message = "Student date of birth is required")
        @Past(message = "Student date of birth must be in the past")
        private LocalDate dob;

        @NotNull(message = "Weight is required")
        @Min(value = 1, message = "Weight must be positive")
        private Integer weight;

        @NotNull(message = "Height is required")
        @Min(value = 1, message = "Height must be positive")
        private Integer height;

        private String medicalConcerns;  // Optional

        @NotBlank(message = "Gender is required")
        private String gender;

        @NotBlank(message = "Belt is required")
        private String belt;

        @NotEmpty(message = "At least one program ID is required")
        private List<Long> programIds;
    }
}
