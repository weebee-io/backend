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

    @Column(name = "content", nullable = false, columnDefinition="TEXT")
    private String quizcontent;

    @Column(name = "subject", nullable = false)
    private String subject;          // 퀴즈 주제

    @Column(name = "quiz_rank")
    @Enumerated(EnumType.STRING)
    private QuizRank quizRank;         // 퀴즈 랭크

    @Column(name = "quiz_level")
    private Integer quizLevel;        // 퀴즈 점수

        // 2지선다 옵션
    @OneToOne(mappedBy = "quiz", fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    private Quiz2Dao option2;
    
        // 4지선다 옵션
    @OneToOne(mappedBy = "quiz", fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    private Quiz4Dao option4;

    @Transient
    public String getCorrectAns() {
        if (quizRank == QuizRank.GOLD) {
            return option4 != null ? option4.getCorrectAns() : null;
        } else {
            return option2 != null ? option2.getCorrectAns() : null;
        }
    }

    public enum QuizRank {
        BRONZE,
        SILVER,
        GOLD;
    }
   
}


