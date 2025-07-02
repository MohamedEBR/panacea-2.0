package com.example.panacea.dto;

import jakarta.validation.constraints.NotEmpty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class WithdrawStudentProgramRequest {

    @NotEmpty(message = "At least one program ID is required for withdrawal")
    private Long programId;
}
