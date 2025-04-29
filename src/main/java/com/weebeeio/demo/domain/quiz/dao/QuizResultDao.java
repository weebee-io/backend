package com.weebeeio.demo.domain.quiz.dao;


import java.time.LocalDateTime;

import jakarta.persistence.*;
import lombok.*;

@Data
@Entity
public class QuizResultDao {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer quiz_id;

    @Column(nullable = false)
    private Integer is_correct;
    
    private String quiz_answer;

    @Column(nullable = false, updatable = false)
    private LocalDateTime quiz_date = LocalDateTime.now();
}
