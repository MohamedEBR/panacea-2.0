package com.example.panacea.repo;

import com.example.panacea.models.StudentProgramHistory;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface StudentProgramHistoryRepository extends JpaRepository<StudentProgramHistory, Long> {
    List<StudentProgramHistory> findByStudentId(Long studentId);
}
