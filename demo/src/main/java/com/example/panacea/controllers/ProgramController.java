package com.example.panacea.controllers;

import com.example.panacea.models.Program;
import com.example.panacea.repo.ProgramRepository;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.math.BigDecimal;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/v1/programs")
@RequiredArgsConstructor
public class ProgramController {

    private final ProgramRepository programRepository;

    @GetMapping
    public List<ProgramDto> list() {
        return programRepository.findAll().stream()
                .map(ProgramDto::from)
                .collect(Collectors.toList());
    }

    @Data
    @AllArgsConstructor
    static class ProgramDto {
        private long id;
        private String name;
        private String description;
        private BigDecimal rate;
        private long durationMinutes;
        private String genderReq;
        private int minAge;
        private String minBelt;
        private int minYearsInClub;
        private int capacity;
        private String daysOfWeek;
        private int enrolledCount;

        static ProgramDto from(Program p) {
        int count = p.getEnrolledStudents() == null ? 0 : p.getEnrolledStudents().size();
        return new ProgramDto(
                    p.getId(),
                    p.getName(),
                    p.getDescription(),
                    p.getRate(),
                    p.getDurationMinutes(),
                    p.getGenderReq() != null ? p.getGenderReq().name() : null,
                    p.getMinAge(),
                    p.getMinBelt() != null ? p.getMinBelt().name() : null,
                    p.getMinYearsInClub(),
                    p.getCapacity(),
            p.getDaysOfWeek(),
            count
            );
        }
    }
}
