package com.example.panacea.models;


import com.example.panacea.enums.Belt;
import com.example.panacea.enums.Gender;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.util.List;

/**
 * Represents a Program that Panacea Karate Academy provides.
 * Each program has a specific schedule, monthly rate, age requirements,
 * belt requirements, and on some special occasions gender requirements.
 * Each program has a student limit. After the limit has been reached,
 * the program won't accept anymore students. It will be locked.
 *
 *
 */
@Entity
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "program")
public class Program {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private long id;

    private String name;
    private String description;
    private BigDecimal rate;

    private long durationMinutes;

    private Gender genderReq;


    private int minAge;
    @Enumerated(EnumType.STRING)
    private Belt minBelt;
    private int minYearsInClub;
    private int capacity;

    // Join table required for many-to-many
    @ManyToMany
    @JoinTable(
            name = "program_student",
            joinColumns = @JoinColumn(name = "program_id"),
            inverseJoinColumns = @JoinColumn(name = "student_id")
    )
    private List<Student> enrolledStudents;

    // Replace list of enums with comma-separated string
    private String daysOfWeek;
}
