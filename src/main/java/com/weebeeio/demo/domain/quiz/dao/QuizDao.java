package com.weebeeio.demo.domain.quiz.dao;

import jakarta.persistence.*;
import lombok.*;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@Data
@Entity
@Table(name = "quiz")
@NoArgsConstructor           // JPA용 무인자 생성자
@AllArgsConstructor          // @Builder와 함께 사용
@Builder
@JsonIgnoreProperties({"hibernateLazyInitializer","handler"})
public class QuizDao {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "quiz_id")
    private Integer quizId;

    @Column(name = "content", nullable = false, columnDefinition = "TEXT")
    private String quizContent;   // camelCase

    @Column(name = "subject", nullable = false)
    private QuizSubject quizSubject;

    @Enumerated(EnumType.STRING)
    @Column(name = "quiz_rank", nullable = false)
    private QuizRank quizRank;

    @Column(name = "quiz_level")
    private Integer quizLevel;

    @OneToOne(mappedBy = "quiz", fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    private Quiz2Dao option2;

    @OneToOne(mappedBy = "quiz", fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    private Quiz4Dao option4;

    @Transient
    public String getCorrectAns() {
        return (quizRank == QuizRank.GOLD)
             ? option4 != null ? option4.getCorrectAns() : null
             : option2 != null ? option2.getCorrectAns() : null;
    }

    public enum QuizRank {
        BRONZE, SILVER, GOLD
    }
    public enum QuizSubject {
        재태크, 금융상식 , 신용소비
    }
}


