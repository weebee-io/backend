package com.weebeeio.demo.domain.quiz.repository;

import com.weebeeio.demo.domain.quiz.dao.QuizEngagementDao;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.Optional;

@Repository
public interface QuizEngagementRepository extends JpaRepository<QuizEngagementDao, Integer> {
    Optional<QuizEngagementDao> findByQuiz_QuizIdAndUser_UserId(Integer quizId, Integer userId);
} 