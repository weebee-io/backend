package com.weebeeio.demo.domain.quiz.dao;

import java.time.LocalDateTime;
import jakarta.persistence.*;
import lombok.Data;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.weebeeio.demo.domain.login.entity.User;

@Data
@Entity
@Table(name = "quiz_result")
@JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
public class QuizResultDao {       // 결과 고유 ID

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "quizResult_id")
    private Integer quizResultId;

    @Column(name = "is_correct", nullable = false)
    private Boolean isCorrect;       // 정답 여부

    @Column(name = "quiz_date", nullable = false, updatable = false)
    private LocalDateTime quizDate = LocalDateTime.now();  // 퀴즈 풀이 시간

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "quiz_id", nullable = false)
    private QuizDao quizId;            // 연결된 퀴즈

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    @JsonIgnore 
    private User user;  // User 엔티티와의 관계
}
