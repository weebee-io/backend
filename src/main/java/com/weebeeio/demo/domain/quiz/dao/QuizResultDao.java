package com.weebeeio.demo.domain.quiz.dao;

import java.time.LocalDateTime;
import jakarta.persistence.*;
import lombok.Data;

@Data
@Entity
@Table(name = "quiz_result")
public class QuizResultDao {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "result_id")
    private Integer resultId;        // 결과 고유 ID

    @Column(name = "is_correct", nullable = false)
    private Boolean isCorrect;       // 정답 여부

    @Column(name = "quiz_date", nullable = false, updatable = false)
    private LocalDateTime quizDate = LocalDateTime.now();  // 퀴즈 풀이 시간

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "quiz_id", nullable = false)
    private QuizDao quiz;            // 연결된 퀴즈
}
