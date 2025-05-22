package com.example.panacea.models;


import com.example.panacea.enums.Belt;
import com.example.panacea.enums.Gender;

import java.time.DayOfWeek;
import java.time.Duration;
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

public class Program {
    private long id;
    private String name;
    private String description;
    private float rate; // typing will be fixed in the future
    private List<DayOfWeek> daysOfWeek;
    private Duration duration;
    private List<Integer> ageReq;
    private List<Gender> genderReq;
    private List<Belt> beltReq;
    private int limit;
    private List<Student> studentsApplied;

    public Program(
        long id,
        String name,
        String description,
        float rate,
        List<DayOfWeek> daysOfWeek,
        Duration duration,
        List<Integer> ageReq,
        List<Gender> genderReq,
        List<Belt> beltReq,
        int limit,
        List<Student> studentsApplied
    ) {
        this.id = id;
        this.name = name;
        this.description = description;
        this.rate = rate;
        this.daysOfWeek = daysOfWeek;
        this.duration = duration;
        this.ageReq = ageReq;
        this.genderReq = genderReq;
        this.beltReq = beltReq;
        this.limit = limit;
        this.studentsApplied = studentsApplied;
    }

}
