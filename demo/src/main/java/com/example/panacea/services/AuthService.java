package com.example.panacea.services;

import com.example.panacea.dto.AuthenticationRequest;
import com.example.panacea.dto.AuthenticationResponse;
import com.example.panacea.dto.RegisterRequest;
import com.example.panacea.enums.Belt;
import com.example.panacea.enums.Gender;
import com.example.panacea.enums.Role;
import com.example.panacea.exceptions.*;
import com.example.panacea.models.Member;
import com.example.panacea.models.Program;
import com.example.panacea.models.Student;
import com.example.panacea.repo.MemberRepository;
import com.example.panacea.repo.ProgramRepository;
import com.stripe.exception.StripeException;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.sql.Date;
import java.time.LocalDate;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class AuthService {

    private final MemberRepository memberRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtService jwtService;
    private final AuthenticationManager authenticationManager;
    private final ProgramRepository programRepository;
    private final StripeService stripeService;

    @Transactional
    public AuthenticationResponse register(RegisterRequest request) throws StripeException {

        if (memberRepository.findByEmail(request.getEmail()).isPresent()) {
            throw new EmailAlreadyExistsException("Email already registered: " + request.getEmail());
        }

        List<Long> allRequestedProgramIds = request.getStudents().stream()
                .flatMap(s -> s.getProgramIds().stream())
                .distinct()
                .toList();

        List<Program> existingPrograms = programRepository.findAllById(allRequestedProgramIds);
        List<Long> existingProgramIds = existingPrograms.stream()
                .map(Program::getId)
                .toList();

        List<Long> missingIds = allRequestedProgramIds.stream()
                .filter(id -> !existingProgramIds.contains(id))
                .toList();

        if (!missingIds.isEmpty()) {
            throw new ProgramNotFoundException("Invalid program IDs: " + missingIds);
        }

        Set<String> uniqueStudentKeys = new HashSet<>();
        boolean hasDuplicateStudents = request.getStudents().stream()
                .anyMatch(s -> !uniqueStudentKeys.add(
                        s.getName().toLowerCase().trim() + "-" + s.getDob()
                ));

        if (hasDuplicateStudents) {
            throw new DuplicateStudentException("Duplicate student detected. Each student (by name and DOB) must be unique within the registration.");
        }

        List<Student> students = request.getStudents().stream().map(s -> {
            List<Program> studentPrograms = programRepository.findAllById(s.getProgramIds());

            Gender gender;
            Belt belt;
            try {
                gender = Gender.valueOf(s.getGender().toUpperCase());
            } catch (IllegalArgumentException e) {
                throw new IllegalArgumentException("Invalid gender: " + s.getGender() + ". Allowed: MALE, FEMALE");
            }

            try {
                belt = Belt.valueOf(s.getBelt().toUpperCase());
            } catch (IllegalArgumentException e) {
                throw new IllegalArgumentException("Invalid belt: " + s.getBelt() + ". Allowed belts: WHITE, YELLOW, etc.");
            }

            if (s.getProgramIds().size() > 5) {
                throw new TooManyProgramsException("Student " + s.getName() + " cannot be enrolled in more than 5 programs.");
            }

            return Student.builder()
                    .name(s.getName())
                    .dob(Date.valueOf(s.getDob()))
                    .weight(s.getWeight())
                    .height(s.getHeight())
                    .medicalConcerns(s.getMedicalConcerns())
                    .gender(gender)
                    .belt(belt)
                    .programs(studentPrograms)
                    .build();
        }).collect(Collectors.toList());


        Member member = Member.builder()
                .name(request.getName())
                .lastName(request.getLastName())
                .email(request.getEmail())
                .password(passwordEncoder.encode(request.getPassword()))
                .dob(Date.valueOf(request.getBirthDate()))
                .phone(request.getPhone())
                .address(request.getAddress())
                .city(request.getCity())
                .postalCode(request.getPostalCode())
                .role(Role.USER)
                .students(students)
                .build();

        String stripeCustomerId;
        try {
            stripeCustomerId = stripeService.createCustomer(
                    request.getName() + " " + request.getLastName(),
                    request.getEmail()
            );
        } catch (StripeException e) {
            throw new StripeIntegrationException("Stripe customer creation failed: " + e.getMessage());
        }

        member.setStripeCustomerId(stripeCustomerId);
        students.forEach(student -> student.setMember(member));

        memberRepository.save(member);

        var jwtToken = jwtService.generateToken(member);
        return AuthenticationResponse.builder()
                .token(jwtToken)
                .build();
    }

    public AuthenticationResponse authenticate(AuthenticationRequest request) {
        authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        request.getEmail(),
                        request.getPassword()
                )
        );

        var member = memberRepository.findByEmail(request.getEmail())
                .orElseThrow(() -> new UsernameNotFoundException(request.getEmail()));

        var jwtToken = jwtService.generateToken(member);
        return AuthenticationResponse.builder()
                .token(jwtToken)
                .build();
    }
}
