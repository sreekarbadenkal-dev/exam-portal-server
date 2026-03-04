package com.exam.model;

import jakarta.persistence.*;
import lombok.*;
import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonProperty;

@Entity
@Table(name = "options")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Option {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", columnDefinition = "BIGINT")
    private Long id;

    @JsonProperty("text")
    private String text;
    
    @Transient // THIS IS THE MAGIC PART
    @JsonProperty("tempId")
    private String tempId;
    
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "question_id") 
    @JsonBackReference
    @ToString.Exclude
    @EqualsAndHashCode.Exclude
    private Question question;
}