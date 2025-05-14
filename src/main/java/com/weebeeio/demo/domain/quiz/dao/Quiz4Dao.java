package com.weebeeio.demo.domain.quiz.dao;

import com.weebeeio.demo.domain.quiz.dao.QuizDao;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "quiz_option_4")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Quiz4Dao {
    
    @Id
    @Column(name = "quiz_id")
    private Integer quizId;

    @OneToOne(fetch = FetchType.LAZY)
    @MapsId
    @JoinColumn(name = "quiz_id")
    private QuizDao quiz;

    @Column(name = "choice_a", nullable = false, columnDefinition = "TEXT")
    private String choiceA;

    @Column(name = "choice_b", nullable = false, columnDefinition = "TEXT")
    private String choiceB;

    @Column(name = "choice_c", nullable = false, columnDefinition = "TEXT")
    private String choiceC;

    @Column(name = "choice_d", nullable = false, columnDefinition = "TEXT")
    private String choiceD;

    @Column(name = "correct_ans", nullable = false)
    private String correctAns;
    
}
