package com.weebeeio.demo.domain.login.entity;

import java.util.List;

import com.weebeeio.demo.domain.quiz.dao.QuizResultDao;

import jakarta.persistence.*;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;

@Entity
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Table(name = "users")
public class User {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "user_id")
    private Integer userId;

    @Column(nullable = false, length = 45, unique = true)
    private String id;

    @Column(nullable = false, length = 45)
    private String password;

    @Column(length = 45, unique = true)
    private String nickname;

    @Column(length = 45)
    private String name;

    @Column(length = 45)
    private String gender;

    @Column(name = "user_rank", nullable = true)
    private String userrank;

    @Column
    private Integer age;

    @Column(name = "user_segment", length = 45, nullable = true)
    private String userSegment;  // 금융이해도 세그먼트

    @OneToMany(mappedBy = "user", fetch = FetchType.LAZY)
    private List<QuizResultDao> quizResults; // 퀴즈 결과 리스트
    
} 