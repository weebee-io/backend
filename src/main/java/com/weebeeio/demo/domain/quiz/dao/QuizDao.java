package com.weebeeio.demo.domain.quiz.dao;

import jakarta.persistence.*;
import lombok.Data;


@Data
@Entity
@Table(name = "quiz")
public class QuizDao {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "quiz_id")
    private Integer quizId;          // 퀴즈 고유 ID

    @Column(name = "user_id", nullable = false)
    private Integer userId;          // 퀴즈를 생성한 사용자 ID

    @Column(name = "subject", nullable = false)
    private String subject;          // 퀴즈 주제

    @Column(name = "quiz_level")
    private String quizLevel;        // 퀴즈 난이도 (EASY, MEDIUM, HARD)

    @Column(name = "quiz_answer", nullable = false)
    private String quizAnswer;       // 퀴즈 정답

   
}


