package com.weebeeio.demo.domain.recommend.dao;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "stats")
@Getter
@NoArgsConstructor
public class UserStats {
    
    @Id
    @Column(name = "user_id")
    private Integer userId;
    
    @Column(name = "stock")
    private Integer stockUnderstanding;
    
    @Column(name = "deposit")
    private Integer depositUnderstanding;
} 