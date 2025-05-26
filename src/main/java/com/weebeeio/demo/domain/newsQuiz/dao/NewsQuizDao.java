package com.weebeeio.demo.domain.newsQuiz.dao;

import com.weebeeio.demo.domain.news.dao.NewsDao;
import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;
import lombok.Builder;

@Entity
@Table(name = "news_quiz")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class NewsQuizDao {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "newsquiz_id")
    private Integer newsquizId;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "news_id", nullable = false)
    private NewsDao news;

    @Column(name = "newsquiz_score", nullable = false)
    private Integer newsquizScore;

    @Column(name = "newsquiz_content", nullable = false, columnDefinition = "TEXT")
    private String newsquizContent;

    @Column(name = "newsquiz_choice_a", nullable = false, columnDefinition = "TEXT")
    private String newsquizChoiceA;

    @Column(name = "newsquiz_choice_b", nullable = false, columnDefinition = "TEXT")
    private String newsquizChoiceB;

    @Column(name = "newsquiz_choice_c", nullable = false, columnDefinition = "TEXT")
    private String newsquizChoiceC;

    @Column(name = "newsquiz_choice_d", nullable = false, columnDefinition = "TEXT")
    private String newsquizChoiceD;

    @Column(name = "newsquiz_correct_ans", nullable = false)
    private String newsquizCorrectAns;
}
