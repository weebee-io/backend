package com.weebeeio.demo.domain.login.entity;

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

    @Column
    private Integer age;

    @Column(name = "user_segment", length = 45)
    private String userSegment;  // 금융이해도 세그먼트
} 