package com.weebeeio.demo.domain.quiz.dao;

import com.fasterxml.jackson.annotation.JsonIgnore;


import jakarta.persistence.*;
import lombok.*;


@Entity
@Table(name = "quiz_option_2")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Quiz2Dao {
    
    @Id
    @Column(name = "quiz_id")
    private Integer quizId;

    @OneToOne(fetch = FetchType.LAZY)
    @MapsId
    @JoinColumn(name = "quiz_id")
    @JsonIgnore 
    private QuizDao quiz;

    @Column(name = "choice_a", nullable = false, columnDefinition = "TEXT")
    private String choiceA;

    @Column(name = "choice_b", nullable = false, columnDefinition = "TEXT")
    private String choiceB;

    @Column(name = "correct_ans", nullable = false)
    private String correctAns;
}
