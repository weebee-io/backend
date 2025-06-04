package com.weebeeio.demo.domain.quiz.repository;

import com.weebeeio.demo.domain.quiz.dao.Quiz2Dao;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface QuizOption2Repository extends JpaRepository<Quiz2Dao, Integer> {

}
