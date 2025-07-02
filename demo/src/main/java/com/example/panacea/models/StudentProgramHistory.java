package com.example.panacea.models;

import jakarta.persistence.*;
import lombok.*;
import java.time.LocalDateTime;

@Entity
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class StudentProgramHistory {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @ManyToOne
    private Student student;

    @ManyToOne
    private Program program;

    @Enumerated(EnumType.STRING)
    private ActionType action;

    private LocalDateTime timestamp;

    public enum ActionType {
        ENROLLED,
        WITHDRAWN
    }
}
