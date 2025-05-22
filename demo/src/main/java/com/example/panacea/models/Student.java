package com.example.panacea.models;


import com.example.panacea.enums.Belt;
import com.example.panacea.enums.Gender;

import java.util.Date;


/**
 * Represents a Student that is registered within Panacea Karate
 * Academy. The Student belt should be promoted with each attended
 * belt exam.
 *
 */

public class Student {
    private long id;
    private String name;
    private Date dob;
    private int weight;
    private int height;
    private String medicalConcerns;
    private Gender gender;
    private String belt;
    private Program program;

    public Student(
            long id,
            String name,
            Date dob,
            int weight,
            int height,
            String medicalConcerns,
            Gender gender,
            Belt belt,
            Program program
    ) {
        this.id = id;
        this.name = name;
        this.dob = dob;
        this.weight = weight;
        this.height = height;
        this.medicalConcerns = medicalConcerns;
        this.gender = gender;
        this.program = program;
    }

}
