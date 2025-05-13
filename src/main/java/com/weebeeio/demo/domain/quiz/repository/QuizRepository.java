package com.weebeeio.demo.domain.quiz.repository;

import com.weebeeio.demo.domain.quiz.dao.QuizDao;


import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface QuizRepository extends JpaRepository<QuizDao, Integer> {

    List<QuizDao> findBySubjectAndQuizLevel(String subject, Integer quizLevel);

    List<QuizDao> findByQuizId(Integer quizId);


}
