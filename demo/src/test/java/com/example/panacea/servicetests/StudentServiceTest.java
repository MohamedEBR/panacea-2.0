// src/test/java/com/example/panacea/services/StudentServiceTest.java
package com.example.panacea.servicetests;

import com.example.panacea.dto.AddStudentProgramRequest;
import com.example.panacea.dto.WithdrawStudentProgramRequest;
import com.example.panacea.dto.UpdateStudentInfoRequest;
import com.example.panacea.enums.Belt;
import com.example.panacea.enums.Gender;
import com.example.panacea.exceptions.*;
import com.example.panacea.models.Program;
import com.example.panacea.models.Student;
import com.example.panacea.repo.ProgramRepository;
import com.example.panacea.repo.StudentProgramHistoryRepository;
import com.example.panacea.repo.StudentRepository;
import com.example.panacea.services.MemberService;
import com.example.panacea.services.StudentService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class StudentServiceTest {

    @Mock
    private StudentRepository studentRepository;

    @Mock
    private ProgramRepository programRepository;

    @Mock
    private StudentProgramHistoryRepository historyRepository;

    @Mock
    private MemberService memberService;

    @InjectMocks
    private StudentService studentService;

    private Student student;
    private Program program;

    @BeforeEach
    void setUp() {
        student = new Student();
        student.setId(10L);
        student.setPrograms(new ArrayList<>());
        student.setDob(LocalDate.of(2010, 1, 1));
        student.setRegisteredAt(LocalDate.now().minusYears(1));
        student.setGender(Gender.MALE);
        lenient().when(studentRepository.findById(10L)).thenReturn(Optional.of(student));

        program = new Program();
        program.setId(100L);
        program.setCapacity(2);
        program.setEnrolledStudents(new ArrayList<>());
        program.setMinAge(5);
        program.setMinBelt(Belt.valueOf("WHITE"));
        program.setMinYearsInClub(0);
        program.setGenderReq(Gender.valueOf("MALE"));
        lenient().when(programRepository.findById(100L)).thenReturn(Optional.of(program));
    }

    @Test
    void updateStudentInfo_Success() {
        UpdateStudentInfoRequest req = new UpdateStudentInfoRequest();
        req.setName("Alice");
        req.setDob( LocalDate.of(2010, 1, 1));
        req.setHeight(150);
        req.setWeight(50);
        req.setMedicalConcerns("None");
        studentService.updateStudentInfo(10L, req);
        verify(studentRepository).save(student);
    }

    @Test
    void addStudentProgram_Success() {
        AddStudentProgramRequest req = new AddStudentProgramRequest(100L);
        studentService.addStudentProgram(10L, req);
        assertTrue(student.getPrograms().contains(program));
        verify(studentRepository).save(student);

        verify(historyRepository).save(any());
    }

    @Test
    void addStudentProgram_AlreadyEnrolled_Throws() {
        student.setPrograms(List.of(program));
        AddStudentProgramRequest req = new AddStudentProgramRequest(100L);
        assertThrows(ProgramAlreadyEnrolledException.class, () ->
                studentService.addStudentProgram(10L, req));
    }

    @Test
    void addStudentProgram_ProgramFull_Throws() {
        program.setEnrolledStudents(List.of(new Student(), new Student()));
        AddStudentProgramRequest req = new AddStudentProgramRequest(100L);
        assertThrows(NoProgramSpaceException.class, () ->
                studentService.addStudentProgram(10L, req));
    }

    @Test
    void addStudentProgram_TooManyPrograms_Throws() {
        student.setPrograms(List.of(program, program, program, program, program));
        Program p2 = new Program(); p2.setId(200L);
        when(programRepository.findById(200L)).thenReturn(Optional.of(p2));
        AddStudentProgramRequest req = new AddStudentProgramRequest(200L);
        assertThrows(TooManyProgramsException.class, () ->
                studentService.addStudentProgram(10L, req));
    }

    @Test
    void withdrawStudentProgram_Success() {
        student.setPrograms(new ArrayList<>(List.of(program)));
        WithdrawStudentProgramRequest req = new WithdrawStudentProgramRequest(100L);
        studentService.withdrawStudentProgram(10L, req);
        assertFalse(student.getPrograms().contains(program));
        verify(studentRepository).save(student);
        verify(memberService).updateMemberStatus(student.getMember());

        verify(historyRepository).save(any());
    }



    @Test
    void withdrawStudentProgram_NotEnrolled_Throws() {
        WithdrawStudentProgramRequest req = new WithdrawStudentProgramRequest(100L);
        assertThrows(StudentNotEnrolledException.class, () ->
                studentService.withdrawStudentProgram(10L, req));
    }
}
