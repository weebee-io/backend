package com.weebeeio.demo.domain.newsQuiz.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.weebeeio.demo.domain.newsQuiz.dao.NewsQuizResultDao;

@Repository
public interface NewsQuizResultRepository extends JpaRepository<NewsQuizResultDao, Integer> {

    @Query("SELECT nqr FROM NewsQuizResultDao nqr WHERE nqr.user.userId = :userId AND nqr.newsquizId.newsquizId = :newsQuizId")
    Optional<NewsQuizResultDao> findByIdandQuizid(@Param("userId") Integer userId, @Param("newsQuizId") Integer newsQuizId);
    
}
