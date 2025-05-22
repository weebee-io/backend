package com.weebeeio.demo.domain.quiz.service;

import com.weebeeio.demo.domain.quiz.dao.QuizDao;
import com.weebeeio.demo.domain.quiz.dao.QuizEngagementDao;
import com.weebeeio.demo.domain.quiz.repository.QuizEngagementRepository;
import com.weebeeio.demo.domain.quiz.repository.QuizRepository;
import com.weebeeio.demo.domain.login.entity.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
public class QuizEngagementService {
    @Autowired
    private QuizEngagementRepository quizEngagementRepository;

    @Autowired
    private QuizRepository quizRepository;

    public QuizEngagementService(QuizEngagementRepository quizEngagementRepository, QuizRepository quizRepository) {
        this.quizEngagementRepository = quizEngagementRepository;
        this.quizRepository = quizRepository;
    }

    public QuizEngagementDao startQuiz(Integer quizId, User user) {
        QuizDao quiz = quizRepository.findById(quizId)
            .orElseThrow(() -> new RuntimeException("퀴즈를 찾을 수 없습니다."));

        QuizEngagementDao engagement = new QuizEngagementDao();
        engagement.setQuiz(quiz);
        engagement.setUser(user);
        engagement.setStartTime(LocalDateTime.now());
        engagement.setIsCompleted(false);

        return quizEngagementRepository.save(engagement);
    }

    public QuizEngagementDao endQuiz(Integer quizId, User user, boolean isCompleted) {
        // 시작 로그가 없는 경우 새로 생성
        QuizEngagementDao engagement = quizEngagementRepository
            .findByQuiz_QuizIdAndUser_UserId(quizId, user.getUserId())
            .orElseGet(() -> {
                QuizDao quiz = quizRepository.findById(quizId)
                    .orElseThrow(() -> new RuntimeException("퀴즈를 찾을 수 없습니다."));
                
                QuizEngagementDao newEngagement = new QuizEngagementDao();
                newEngagement.setQuiz(quiz);
                newEngagement.setUser(user);
                newEngagement.setStartTime(LocalDateTime.now().minusMinutes(1)); // 1분 전으로 설정
                return newEngagement;
            });

        engagement.setEndTime(LocalDateTime.now());
        engagement.setIsCompleted(isCompleted);

        return quizEngagementRepository.save(engagement);
    }
} 