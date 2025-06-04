package com.weebeeio.demo.domain.quiz.repository;

import com.weebeeio.demo.domain.quiz.dao.QuizResultDao;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface QuizResultRepository extends JpaRepository<QuizResultDao, Integer> {

    List<QuizResultDao> findByUser_UserId(Integer userId);

    

    Optional<QuizResultDao> findByUserUserIdAndQuizIdQuizId(Integer userId, Integer quizId);



    List<QuizResultDao> findAllByUser_UserId(Integer userId);

    @Query("SELECT COUNT(qr) FROM QuizResultDao qr WHERE qr.quizId.quizId = :quizId")
    Long countTotalAttemptsByQuizId(@Param("quizId") Integer quizId);

    @Query("SELECT COUNT(qr) FROM QuizResultDao qr WHERE qr.quizId.quizId = :quizId AND qr.isCorrect = true")
    Long countCorrectAttemptsByQuizId(@Param("quizId") Integer quizId);
}
