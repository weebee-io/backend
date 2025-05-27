package com.weebeeio.demo.domain.newsQuiz.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.weebeeio.demo.domain.newsQuiz.dao.NewsQuizDao;

public interface NewsQuizRepository extends JpaRepository<NewsQuizDao, Integer> {
    
    
}
