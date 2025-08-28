package com.example.panacea.repotests;

import com.example.panacea.models.Member;
import com.example.panacea.models.Student;
import com.example.panacea.repo.MemberRepository;
import com.example.panacea.repo.StudentRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.annotation.ComponentScan;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

@DataJpaTest
@EntityScan("com.example.panacea.models")
@ComponentScan(basePackages = "com.example.panacea.repo")
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.ANY)
class MemberRepositoryTest {

    @Autowired
    private MemberRepository memberRepository;

    @Autowired
    private StudentRepository studentRepository;

    @Test
    void findByStudents_ShouldReturnMember() {
        // Create and save a member
        Member member = new Member();
        member.setName("John");
        member.setLastName("Doe");
        member = memberRepository.save(member);

        // Create students and assign the member
        Student student1 = new Student();
        student1.setName("Test Student1");
        student1.setDob(LocalDate.of(2010, 6, 15));
        student1.setMember(member);  // link member

        Student student2 = new Student();
        student2.setName("Test Student2");
        student2.setDob(LocalDate.of(2010, 6, 15));
        student2.setMember(member);  // link member

        // Save students
        student1 = studentRepository.save(student1);
        student2 = studentRepository.save(student2);

        // Update member’s student list (optional, depends on owning side)
        List<Student> students = new ArrayList<>();
        students.add(student1);
        students.add(student2);
        member.setStudents(students);
        memberRepository.save(member);

        // Test the repo query
        Optional<Member> result = memberRepository.findByStudents_Id(student1.getId());
        assertTrue(result.isPresent());
        assertEquals("John", result.get().getName());
    }

}
