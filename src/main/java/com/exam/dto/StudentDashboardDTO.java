package com.exam.dto;

import java.util.List;

import lombok.Data;

@Data
public class StudentDashboardDTO {
    private List<StudentExamDTO> pending;
    private List<ResultDTO> attempted;
}
