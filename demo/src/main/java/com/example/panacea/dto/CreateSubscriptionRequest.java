package com.example.panacea.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CreateSubscriptionRequest {
    private Long memberId;
    private List<StudentProgramSelection> students;

    public static class StudentProgramSelection {
        public Long studentId;
        public List<Long> programIds;
    }
}
