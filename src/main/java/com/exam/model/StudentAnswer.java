package com.exam.model;

import jakarta.persistence.*;
import lombok.Data;
import com.fasterxml.jackson.annotation.JsonIgnore;

@Entity
@Data
public class StudentAnswer {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private Long questionId;
    private Long selectedOptionId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "result_id")
    @JsonIgnore // Prevent infinite recursion during JSON serialization
    private Result result;
}