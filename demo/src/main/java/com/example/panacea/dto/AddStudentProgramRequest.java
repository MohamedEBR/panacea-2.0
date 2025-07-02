package com.example.panacea.dto;

import com.example.panacea.models.Program;
import jakarta.validation.constraints.NotEmpty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class AddStudentProgramRequest {

    @NotEmpty(message = "A program id is required")
    private Long programId;

}
