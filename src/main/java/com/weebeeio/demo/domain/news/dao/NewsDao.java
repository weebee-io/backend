package com.weebeeio.demo.domain.news.dao;

import java.util.ArrayList;
import java.util.List;


import com.weebeeio.demo.domain.newsQuiz.dao.NewsQuizDao;

import jakarta.persistence.*;
import lombok.*;
    
@Entity
@Table(name = "news")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class NewsDao {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "news_id")
    private Integer newsId;

    @Column(nullable = false, length = 255)
    private String title;

    @Column(columnDefinition = "TEXT")
    private String description;

    @Column(nullable = false, length = 255)
    private String url;

    @Column(name = "published_date")
    private java.time.LocalDateTime publishedDate;

    @Column(nullable = false, length = 100)
    private String source;

    @Column(name = "created_at")
    private java.time.LocalDateTime createdAt;


    @OneToMany(
        mappedBy = "news",
        cascade = CascadeType.ALL,
        orphanRemoval = true,
        fetch = FetchType.LAZY
    )
    @Builder.Default
    private List<NewsQuizDao> newsQuizzes = new ArrayList<>();
}