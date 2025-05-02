package com.weebeeio.demo.domain.quiz.dao;

import jakarta.persistence.*;
import lombok.Data;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@Data
@Entity
@Table(name = "quiz")
@JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
public class QuizDao {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "quiz_id")
    private Integer quizId;          // 퀴즈 고유 ID

    @Column(name = "subject", nullable = false)
    private String subject;          // 퀴즈 주제

    @Column(name = "quiz_level")
    private Integer quizLevel;        // 퀴즈 난이도 (1,3,5) 실버 브론즈 골드

    @Column(name = "quiz_answer", nullable = false)
    private String quizAnswer;       // 퀴즈 정답

   
}


