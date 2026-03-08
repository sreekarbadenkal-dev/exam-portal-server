package com.exam.model;

import jakarta.persistence.*;
import lombok.*;

import java.util.ArrayList;
import java.util.List;

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
    @ManyToOne(fetch = FetchType.EAGER) // updated well now we need it eagerly LAZY because we usually just need the Exam ID, not the whole exam tree again
    @JoinColumn(name = "exam_id")
    @JsonIgnoreProperties({"department"}) // Prevents sending the entire question bank inside a result
    @ToString.Exclude // Prevents infinite loop in logs
    @EqualsAndHashCode.Exclude
    private Exam exam;
    
 // Inside Result.java
    @OneToMany(mappedBy = "result", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.EAGER)
    private List<StudentAnswer> studentAnswers = new ArrayList<>();

    // 2. Linking to the Student
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "student_id")
    @JsonIgnoreProperties({"password", "results"}) // Security: Don't send the password back; Logic: Don't loop back to results
    @ToString.Exclude
    @EqualsAndHashCode.Exclude
    private Student student;
}