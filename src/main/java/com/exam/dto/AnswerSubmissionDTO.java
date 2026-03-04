package com.exam.dto;

import lombok.Data;
import java.util.Map;

@Data
public class AnswerSubmissionDTO {
    private Long examid;
    private Long studentid;
    // Key: Question ID, Value: Selected Option ID (or Text)
    private Map<Long, String> selectedOptions; 
}