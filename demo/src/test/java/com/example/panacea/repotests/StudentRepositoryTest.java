package com.example.panacea.repotests;

import com.example.panacea.enums.Belt;
import com.example.panacea.enums.Gender;
import com.example.panacea.models.Member;
import com.example.panacea.models.Student;
import com.example.panacea.repo.StudentRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.annotation.ComponentScan;

import java.time.LocalDate;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

@DataJpaTest
@EntityScan("com.example.panacea.models")  // Add this line
@ComponentScan(basePackages = "com.example.panacea.repo")  // So Spring wires the repo beans
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.ANY)
class StudentRepositoryTest {

    @Autowired
    private StudentRepository studentRepository;

    @Test
    void saveAndFindById_ShouldReturnStudent() {
        Member member = new Member();
        member.setName("John");
        member.setLastName("Doe");
        member.setEmail("hello@mail.com");
        member.setPassword("password");
        member.setDob(LocalDate.of(1980, 1, 1));
        member.setAddress("someAddress");
        member.setCity("someCity");
        member.setPhone("1234");
        member.setPostalCode("12345");

        Student student = new Student();
        student.setName("Alice");
        student.setDob(LocalDate.of(2012, 5, 20));
        student.setGender(Gender.FEMALE);
        student.setBelt(Belt.WHITE);
        student.setMember(member);
        student.setWeight(5);
        student.setHeight(20);

        Student saved = studentRepository.save(student);

        Optional<Student> found = studentRepository.findById(saved.getId());
        assertTrue(found.isPresent());
        assertEquals("Alice", found.get().getName());
    }
}