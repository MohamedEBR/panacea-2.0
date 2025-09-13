package com.example.panacea.dto;

import jakarta.validation.constraints.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UpdateMemberInfoRequest {

    @NotBlank(message = "Name is required")
    private String name;

    @NotBlank(message = "Last name is required")
    private String lastName;

    @NotNull(message = "Date of birth is required")
    @Past(message = "Date of birth must be in the past")
    private LocalDate dob;

    @NotBlank(message = "Phone number is required")
    @Pattern(
            regexp = "^[0-9\\-\\+]{9,15}$",
            message = "Invalid phone number format"
    )

    private String phone;

    @NotBlank(message = "Address is required")
    private String address;

    @NotBlank(message = "City is required")
    private String city;

    @NotBlank(message = "Postal code is required")
    @Pattern(
            regexp = "^[A-Za-z]\\d[A-Za-z][ -]?\\d[A-Za-z]\\d$",
            message = "Invalid Canadian postal code format"
    )
    private String postalCode;
}
