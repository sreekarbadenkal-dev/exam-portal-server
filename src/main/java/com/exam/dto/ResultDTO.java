package com.exam.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ResultDTO {
    private Long id;              // Matches result.id in React
    private String examTitle;
    private int marksGot;
    private int correctAnswers;   // New field
    private String submittedAt;   // New field
}