package com.example.panacea.services;

import com.example.panacea.dto.AddStudentProgramRequest;
import com.example.panacea.dto.UpdateStudentInfoRequest;
import com.example.panacea.dto.WithdrawStudentProgramRequest;
import com.example.panacea.enums.Belt;
import com.example.panacea.exceptions.*;
import com.example.panacea.models.Program;
import com.example.panacea.models.Student;
import com.example.panacea.models.StudentProgramHistory;
import com.example.panacea.repo.ProgramRepository;
import com.example.panacea.repo.StudentProgramHistoryRepository;
import com.example.panacea.repo.StudentRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.Period;

@Service
@RequiredArgsConstructor
public class StudentService {

    private final StudentRepository studentRepository;
    private final ProgramRepository programRepository;
    private final MemberService memberService;
    private final StudentProgramHistoryRepository historyRepository;
    public Student getStudentById(Long id) {
        return studentRepository.findById(id)
                .orElseThrow(() -> new StudentNotFoundException("Student not found with id: " + id));
    }

    @Transactional
    public void updateStudentInfo(Long id, UpdateStudentInfoRequest request) {
        Student student = getStudentById(id);

        student.setName(request.getName());
        student.setHeight(request.getHeight());
        student.setWeight(request.getWeight());
        student.setDob(request.getDob());
        student.setMedicalConcerns(request.getMedicalConcerns());

        studentRepository.save(student);
    }

    @Transactional
    public void addStudentProgram(Long id, AddStudentProgramRequest request) {
        Student student = getStudentById(id);
        Program program = programRepository.findById(request.getProgramId())
                .orElseThrow(() -> new ProgramNotFoundException("Program not found with id: " + request.getProgramId()));

        if (student.getPrograms().contains(program)) {
            throw new ProgramAlreadyEnrolledException("Student is already enrolled in this program.");
        }

        if (student.getPrograms().size() >= 5) {
            throw new TooManyProgramsException("Student cannot be enrolled in more than 5 programs.");
        }

        if (program.getEnrolledStudents().size() >= program.getCapacity()) {
            throw new NoProgramSpaceException("Program is full.");
        }

        int age = Period.between(student.getDob(), LocalDate.now()).getYears();
        if (age < program.getMinAge()) {
            throw new ProgramRequirementNotMetException("Student does not meet the minimum age requirement.");
        }

        Belt requiredBelt = program.getMinBelt();
        if (student.getBelt() != null && (student.getBelt().getRank() < requiredBelt.getRank())) {
            throw new ProgramRequirementNotMetException("Student's belt is too low for this program.");
        }


        if (student.getYearsInClub() < program.getMinYearsInClub()) {
            throw new ProgramRequirementNotMetException("Student does not meet the experience requirement.");
        }

        if (program.getGenderReq() != null) {
            if (program.getGenderReq() != student.getGender()) {
                throw new ProgramRequirementNotMetException("Student does not meet the gender requirement.");
            }
        }

        StudentProgramHistory studentProgramHistory = StudentProgramHistory.builder()
                .student(student)
                .program(program)
                .action(StudentProgramHistory.ActionType.ENROLLED)
                .timestamp(LocalDateTime.now())
                .build();

        historyRepository.save(studentProgramHistory);


        student.getPrograms().add(program);
        studentRepository.save(student);
    }

    @Transactional
    public void withdrawStudentProgram(Long id, WithdrawStudentProgramRequest request) {
        Student student = getStudentById(id);
        Program program = programRepository.findById(request.getProgramId())
                .orElseThrow(() -> new ProgramNotFoundException("Program not found with id: " + request.getProgramId()));

        if (!student.getPrograms().contains(program)) {
            throw new StudentNotEnrolledException("Student is not enrolled in this program.");
        }

        historyRepository.save(StudentProgramHistory.builder()
                .student(student)
                .program(program)
                .action(StudentProgramHistory.ActionType.WITHDRAWN)
                .timestamp(LocalDateTime.now())
                .build());


        student.getPrograms().remove(program);
        studentRepository.save(student);


        memberService.updateMemberStatus(student.getMember());
    }

    @Transactional
    public void deleteStudent(Long id) {
        Student student = getStudentById(id);
        studentRepository.delete(student);
        memberService.updateMemberStatus(student.getMember());
    }
}
