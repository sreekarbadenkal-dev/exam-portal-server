package com.exam.model;

import jakarta.persistence.*;
import lombok.*;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@Entity
@Table(name = "results", uniqueConstraints= {@UniqueConstraint (name="uniquekeyconstraint_examid_studentid",columnNames= {"student_id","exam_id"})})
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Result {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private Integer marksGot;
    private Integer correctAnswers;
    private String examTitle;
    private String submittedAt;

    // 1. Linking to the Exam
    @ManyToOne(fetch = FetchType.LAZY) // LAZY because we usually just need the Exam ID, not the whole exam tree again
    @JoinColumn(name = "exam_id")
    @JsonIgnoreProperties({"questions", "department"}) // Prevents sending the entire question bank inside a result
    @ToString.Exclude // Prevents infinite loop in logs
    @EqualsAndHashCode.Exclude
    private Exam exam;

    // 2. Linking to the Student
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "student_id")
    @JsonIgnoreProperties({"password", "results"}) // Security: Don't send the password back; Logic: Don't loop back to results
    @ToString.Exclude
    @EqualsAndHashCode.Exclude
    private Student student;
}