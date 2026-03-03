package com.exam.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name = "exams")
@Data // Generates Getters, Setters, toString, equals, and hashCode
@NoArgsConstructor // Required by JPA
@AllArgsConstructor // Useful for testing
public class Exam {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", columnDefinition = "BIGINT")
    private Long id;

    private String title;
    private Integer examtime;
    private Integer semester;
    private Integer passpercentage;

    @ElementCollection(fetch = FetchType.EAGER)
    @CollectionTable(name = "exam_departments", joinColumns = @JoinColumn(name = "exam_id"))
    @Column(name = "department_name")
    private Set<String> department = new HashSet<>();

    @OneToMany(cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.EAGER)
    @JoinColumn(name = "exam_id")
    private Set<Question> questions = new HashSet<>(); // Change List to Set
}