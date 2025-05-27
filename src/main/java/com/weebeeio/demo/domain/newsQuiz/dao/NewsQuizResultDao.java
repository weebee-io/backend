package com.weebeeio.demo.domain.newsQuiz.dao;

import java.time.LocalDateTime;
import java.util.Optional;

import jakarta.persistence.*;
import lombok.Data;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.weebeeio.demo.domain.login.entity.User;

@Data
@Entity
@Table(name = "newsquiz_result")
@JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
public class NewsQuizResultDao {       // 결과 고유 ID

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "newsquizResult_id")
    private Integer newsquizResultId;

    @Column(name = "is_correct", nullable = false)
    private Boolean isCorrect;       // 정답 여부

    @Column(name = "newsquiz_date", nullable = false, updatable = false)
    private LocalDateTime newsquizDate = LocalDateTime.now();  // 퀴즈 풀이 시간

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "newsquiz_id", nullable = false)
    private NewsQuizDao newsquizId;            // 연결된 퀴즈

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    @JsonIgnore 
    private User user;  // User 엔티티와의 관계


}
