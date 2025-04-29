package com.weebeeio.demo.domain.quiz.dao;

import jakarta.persistence.*;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@Entity
public class QuizDao {

    //퀴즈번호
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer quiz_id;

    @Column(nullable = false)
    private Integer user_id; // 제목
    private String quiz_level;
    private String quiz_answer;

}


