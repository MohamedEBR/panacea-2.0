package com.example.panacea.controllers;

import com.example.panacea.dto.AddStudentProgramRequest;
import com.example.panacea.dto.UpdateStudentInfoRequest;
import com.example.panacea.dto.WithdrawStudentProgramRequest;
import com.example.panacea.models.StudentProgramHistory;
import com.example.panacea.repo.StudentProgramHistoryRepository;
import com.example.panacea.services.StudentService;
import com.example.panacea.services.DataOwnershipService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/students")
@RequiredArgsConstructor
@PreAuthorize("hasRole('USER')")
public class StudentController {

    private final StudentService service;
    private final StudentProgramHistoryRepository historyRepository;
    private final DataOwnershipService dataOwnershipService;


    @GetMapping("/{id}")
    public ResponseEntity<?> getStudent(@PathVariable Long id) {
        dataOwnershipService.validateStudentAccess(id);
        return ResponseEntity.ok(service.getStudentById(id));
    }

    @PutMapping("/{id}")
    public ResponseEntity<String> updateStudentInfo(@PathVariable Long id, @RequestBody UpdateStudentInfoRequest request) {
        dataOwnershipService.validateStudentAccess(id);
        service.updateStudentInfo(id, request);
        return ResponseEntity.ok("Student info updated successfully");
    }

    @PutMapping(value = "/{id}/programs", params = "add")
    public ResponseEntity<String> addStudentProgram(@PathVariable Long id, @RequestBody AddStudentProgramRequest request) {
        dataOwnershipService.validateStudentAccess(id);
        service.addStudentProgram(id, request);
        return ResponseEntity.ok("Student program added successfully");
    }

    @PutMapping(value = "/{id}/programs", params = "withdraw")
    public ResponseEntity<String> withdrawStudentProgram(@PathVariable Long id, @RequestBody WithdrawStudentProgramRequest request) {
        dataOwnershipService.validateStudentAccess(id);
        service.withdrawStudentProgram(id, request);
        return ResponseEntity.ok("Student program withdrawn successfully");
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<String> deleteStudent(@PathVariable Long id) {
        dataOwnershipService.validateStudentAccess(id);
        service.deleteStudent(id);
        return ResponseEntity.ok("Student withdrawn successfully");
    }

    @GetMapping("/{id}/history")
    public ResponseEntity<List<StudentProgramHistory>> getStudentHistory(@PathVariable Long id) {
        dataOwnershipService.validateStudentAccess(id);
        return ResponseEntity.ok(historyRepository.findByStudentId(id));
    }

}
