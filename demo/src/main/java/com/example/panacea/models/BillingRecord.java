package com.example.panacea.models;

import jakarta.persistence.*;
import lombok.*;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

@Entity
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "billing_record")
public class BillingRecord {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    private BigDecimal amount;

    private LocalDateTime date;

    private String stripeTransactionId;

    @ManyToMany
    @JoinTable(
            name = "billing_record_students",
            joinColumns = @JoinColumn(name = "billing_record_id"),
            inverseJoinColumns = @JoinColumn(name = "student_id")
    )
    private List<Student> billedStudents;

    @ManyToOne
    @JoinColumn(name = "member_id")
    private Member member;
}
