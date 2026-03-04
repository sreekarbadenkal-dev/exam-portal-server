package com.exam.model;

import jakarta.persistence.*;
import lombok.*;
import java.util.HashSet;
import java.util.Set;
import com.fasterxml.jackson.annotation.JsonManagedReference;

@Entity
@Table(name = "exams")
@Data 
@NoArgsConstructor 
@AllArgsConstructor 
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

    @OneToMany(mappedBy = "exam", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.EAGER)
    @JsonManagedReference
    @ToString.Exclude
    @EqualsAndHashCode.Exclude
    private Set<Question> questions = new HashSet<>();
}