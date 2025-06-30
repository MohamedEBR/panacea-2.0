package com.example.panacea.models;


import com.example.panacea.enums.Belt;
import com.example.panacea.enums.Gender;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;
import java.util.List;


/**
 * Represents a Student that is registered within Panacea Karate
 * Academy. The Student belt should be promoted with each attended
 * belt exam.
 *
 */


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
    private Date dob;

    private int weight;
    private int height;
    private String medicalConcerns;

    @Enumerated(EnumType.STRING)
    private Gender gender;

    @Enumerated(EnumType.STRING)
    private Belt belt;

    @ManyToMany(mappedBy = "enrolledStudents")
    private List<Program> programs;
}

