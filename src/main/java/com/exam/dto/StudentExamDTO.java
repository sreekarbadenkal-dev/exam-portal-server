package com.exam.dto;

import lombok.*;
import java.util.Set;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class StudentExamDTO {

    private Long id;
    private String title;
    private Integer examtime;
    private Integer semester;
    private Set<String> department;
    private Set<StudentQuestionDTO> questions;
}