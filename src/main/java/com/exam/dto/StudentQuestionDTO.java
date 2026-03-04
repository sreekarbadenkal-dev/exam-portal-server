package com.exam.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;
import java.util.Set;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class StudentQuestionDTO {

    private Long id;

    @JsonProperty("questiontext") // Matches your React question.questiontext
    private String questiontext;

    @JsonProperty("options")
    private Set<StudentOptionDTO> options;
}