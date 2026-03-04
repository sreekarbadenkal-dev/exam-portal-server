package com.exam.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class StudentOptionDTO {
    
    private Long id;

    @JsonProperty("text") // Matches your React option.text logic
    private String text;
}