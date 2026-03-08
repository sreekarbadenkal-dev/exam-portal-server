package com.exam.dto;

import com.exam.model.Exam;
import com.exam.model.StudentAnswer;
import lombok.Data;
import java.util.List;

@Data
public class ResultDTO {
    private Long id;
    private String examTitle;
    private Integer marksGot;
    private Integer correctAnswers;
    private String submittedAt;

    // ADD THESE TWO FIELDS
    private Exam exam; 
    private List<StudentAnswer> studentAnswers; 
}