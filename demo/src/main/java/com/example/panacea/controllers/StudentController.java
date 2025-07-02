package com.example.panacea.controllers;

import com.example.panacea.dto.AddStudentProgramRequest;
import com.example.panacea.dto.UpdateStudentInfoRequest;
import com.example.panacea.dto.WithdrawStudentProgramRequest;
import com.example.panacea.models.StudentProgramHistory;
import com.example.panacea.repo.StudentProgramHistoryRepository;
import com.example.panacea.services.StudentService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/students")
@RequiredArgsConstructor
public class StudentController {

    private final StudentService service;
    private final StudentProgramHistoryRepository historyRepository;


    @GetMapping("/{id}")
    public ResponseEntity<?> getStudent(@PathVariable Long id) {
        return ResponseEntity.ok(service.getStudentById(id));
    }

    @PutMapping("/{id}")
    public ResponseEntity<String> updateStudentInfo(@PathVariable Long id, @RequestBody UpdateStudentInfoRequest request) {
        service.updateStudentInfo(id, request);
        return ResponseEntity.ok("Student info updated successfully");
    }

    @PutMapping("/{id}/programs")
    public ResponseEntity<String> addStudentProgram(@PathVariable Long id, @RequestBody AddStudentProgramRequest request) {
        service.addStudentProgram(id, request);
        return ResponseEntity.ok("Student program added successfully");
    }

    @PutMapping("/{id}/programs")
    public ResponseEntity<String> withdrawStudentProgram(@PathVariable Long id, @RequestBody WithdrawStudentProgramRequest request) {
        service.withdrawStudentProgram(id, request);
        return ResponseEntity.ok("Student program withdrawn successfully");
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<String> deleteStudent(@PathVariable Long id) {
        service.deleteStudent(id);
        return ResponseEntity.ok("Student withdrawn successfully");
    }

    @GetMapping("/{id}/history")
    public ResponseEntity<List<StudentProgramHistory>> getStudentHistory(@PathVariable Long id) {
        return ResponseEntity.ok(historyRepository.findByStudentId(id));
    }

}
