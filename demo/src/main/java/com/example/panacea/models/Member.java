package com.example.panacea.models;

import java.util.Date;
import java.util.List;

/**
 * Represents a Member that is part of Panacea Karate
 * Academy. The member should be able to register Students
 * into the Academy's Program, remove, or add students at their
 * leisure.
 *
 */

public class Member {
    private long id;
    private String name;
    private String lastName;
    private String email;
    private Date age;
    private String phone;
    private List<Student> students;
    private String address;
    private String city;
    private String postalCode;
    private String History;

    public Member(
            long id,
            String name,
            String lastName,
            String email,
            Date age,
            String phone,
            List<Student> students,
            String address,
            String city,
            String postalCode,
            String History) {
        this.id = id;
        this.name = name;
        this.lastName = lastName;
        this.email = email;
        this.age = age;
        this.students = students;
        this.phone = phone;
        this.address = address;
        this.city = city;
        this.postalCode = postalCode;
        this.History = History;

    }

}
