package com.example.panacea.models;

import com.example.panacea.enums.Belt;
import com.example.panacea.enums.Gender;
import com.fasterxml.jackson.annotation.JsonBackReference;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDate;
import java.time.Period;
import java.util.Date;
import java.util.List;

@Entity
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "student")
public class Student {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private long id;

    private String name;

    @Temporal(TemporalType.DATE)
    private LocalDate dob;

    private int weight;
    private int height;
    private String medicalConcerns;

    @Enumerated(EnumType.STRING)
    private Gender gender;

    @Enumerated(EnumType.STRING)
    private Belt belt;

    private LocalDate registeredAt;
    private int yearsInClub;

    @ManyToOne
    @JoinColumn(name = "member_id")
    @JsonBackReference
    private Member member;

    @ManyToMany(mappedBy = "enrolledStudents")
    private List<Program> programs;

    public int getYearsInClub() {
        if (registeredAt == null) return 0;
        return Period.between(registeredAt, LocalDate.now()).getYears();
    }

}
