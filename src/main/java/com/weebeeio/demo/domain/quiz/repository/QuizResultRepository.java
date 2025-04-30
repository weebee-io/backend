package com.weebeeio.demo.domain.quiz.repository;

import com.weebeeio.demo.domain.quiz.dao.QuizResultDao;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface QuizResultRepository extends JpaRepository<QuizResultDao, Integer> {

    List<QuizResultDao> findByUserId(Integer userId);


}
