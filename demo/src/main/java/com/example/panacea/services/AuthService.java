package com.example.panacea.services;

import com.example.panacea.dto.AuthenticationRequest;
import com.example.panacea.dto.AuthenticationResponse;
import com.example.panacea.dto.RegisterRequest;
import com.example.panacea.enums.Belt;
import com.example.panacea.enums.Gender;
import com.example.panacea.enums.MemberStatus;
import com.example.panacea.enums.Role;
import com.example.panacea.enums.StudentStatus;
import com.example.panacea.exceptions.*;
import com.example.panacea.models.Member;
import com.example.panacea.models.Program;
import com.example.panacea.models.Student;
import com.example.panacea.repo.MemberRepository;
import com.example.panacea.repo.ProgramRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.Period;
import java.util.ArrayList;
import java.util.List;


@Service
@RequiredArgsConstructor
public class AuthService {

    private final MemberRepository memberRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtService jwtService;
    private final AuthenticationManager authenticationManager;
    private final ProgramRepository programRepository;


    public AuthenticationResponse register(RegisterRequest request) {
        // Normalize email: trim and lowercase for consistent storage and lookup
        if (request.getEmail() != null) {
            request.setEmail(request.getEmail().trim().toLowerCase());
        }
        List<Student> students = request.getStudents().stream().map(s -> {
            // Expect exactly one program id
            if (s.getProgramIds() == null || s.getProgramIds().size() != 1) {
                throw new ProgramRequirementNotMetException("Exactly one program must be selected for each student");
            }
            Long programId = s.getProgramIds().get(0);
            Program program = programRepository.findById(programId)
                    .orElseThrow(() -> new ProgramNotFoundException("Program not found: " + programId));

            LocalDate dob = s.getDob();
            int age = Period.between(dob, LocalDate.now()).getYears();
            Belt studentBelt = Belt.valueOf(s.getBelt().toUpperCase());
            LocalDate registeredAt = LocalDate.now();

            if (program.getEnrolledStudents() != null && program.getEnrolledStudents().size() >= program.getCapacity()) {
                throw new NoProgramSpaceException("Program is full: " + program.getName());
            }

            if (age < program.getMinAge()) {
                throw new ProgramRequirementNotMetException("Student is too young for program: " + program.getName());
            }

            if (studentBelt.getRank() < program.getMinBelt().getRank()) {
                throw new ProgramRequirementNotMetException("Student belt too low for program: " + program.getName());
            }

            int yearsInClub = Period.between(registeredAt, LocalDate.now()).getYears(); // 0 initially
            if (yearsInClub < program.getMinYearsInClub()) {
                throw new ProgramRequirementNotMetException("Student lacks experience for program: " + program.getName());
            }

        Student student = Student.builder()
            .name(s.getName())
            .dob(dob)
            .weight(s.getWeight())
            .height(s.getHeight())
            .medicalConcerns(s.getMedicalConcerns())
            .gender(Gender.valueOf(s.getGender()))
            .belt(studentBelt)
            .registeredAt(registeredAt)
            // Mark as FROZEN until payment completes (webhook will activate)
            .status(StudentStatus.FROZEN)
            .build();

        // Set up bidirectional relation using MUTABLE lists
        student.setPrograms(new ArrayList<>());
        student.getPrograms().add(program);

        if (program.getEnrolledStudents() == null) {
        program.setEnrolledStudents(new ArrayList<>());
        }
        program.getEnrolledStudents().add(student);

        return student;
        }).toList();

        Member member = Member.builder()
                .name(request.getName())
                .lastName(request.getLastName())
        .email(request.getEmail())
                .password(passwordEncoder.encode(request.getPassword()))
                .dob(request.getBirthDate())
                .phone(request.getPhone())
                .address(request.getAddress())
                .unit(request.getUnit())
                .city(request.getCity())
                .postalCode(request.getPostalCode())
                .role(Role.USER)
                .status(MemberStatus.ACTIVE)
                .students(students)
                .build();

        students.forEach(student -> student.setMember(member));

        memberRepository.save(member);

    var jwtToken = jwtService.generateToken(member);
    return AuthenticationResponse.builder()
        .token(jwtToken)
        .id(member.getId())
        .email(member.getEmail())
        .name(member.getName())
        .lastName(member.getLastName())
        .role(member.getRole().name())
        .build();
    }



    public AuthenticationResponse authenticate(AuthenticationRequest request) {
    // Normalize email for lookup and authentication
    String email = request.getEmail() != null ? request.getEmail().trim().toLowerCase() : null;
    authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
            email,
                        request.getPassword()
                )
        );

    var member = memberRepository.findByEmail(email)
        .orElseThrow(() -> new UsernameNotFoundException(email));

    var jwtToken = jwtService.generateToken(member);
    return AuthenticationResponse.builder()
        .token(jwtToken)
        .id(member.getId())
        .email(member.getEmail())
        .name(member.getName())
        .lastName(member.getLastName())
        .role(member.getRole().name())
        .build();
    }
}
