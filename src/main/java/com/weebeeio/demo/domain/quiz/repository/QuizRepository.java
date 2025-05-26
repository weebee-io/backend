package com.weebeeio.demo.domain.quiz.repository;

import com.weebeeio.demo.domain.quiz.dao.QuizDao;
import com.weebeeio.demo.domain.quiz.dao.QuizDao.QuizRank;
import com.weebeeio.demo.domain.quiz.dao.QuizDao.QuizSubject;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface QuizRepository extends JpaRepository<QuizDao, Integer> {

    List<QuizDao> findByQuizSubjectAndQuizRank(QuizSubject quizSubject, QuizRank quizrank);

    List<QuizDao> findByQuizId(Integer quizId);


}
