package com.weebeeio.demo.domain.newsQuiz.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.weebeeio.demo.domain.newsQuiz.dao.NewsQuizResultDao;

public interface NewsQuizResultRepository extends JpaRepository<NewsQuizResultDao, Integer> {

    Optional<NewsQuizResultDao> findByIdandQuizid(Integer userId, Integer newsQuizId);
    
}
