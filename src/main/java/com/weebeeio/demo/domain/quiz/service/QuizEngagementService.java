package com.weebeeio.demo.domain.quiz.service;

import com.weebeeio.demo.domain.quiz.dao.QuizDao;
import com.weebeeio.demo.domain.quiz.repository.QuizRepository;
import com.weebeeio.demo.domain.login.entity.User;
import com.weebeeio.demo.global.analytics.AnalyticsLogger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.time.Duration;

/**
 * QuizEngagementService
 */
@Service
public class QuizEngagementService {
    @Autowired
    private QuizRepository quizRepository;

    /**
     * QuizEngagementService Constructor
     * 
     * @param quizRepository QuizRepository
     */
    public QuizEngagementService(QuizRepository quizRepository) {
        this.quizRepository = quizRepository;
    }

    /**
     * 퀴즈 시작 로그 기록
     * 
     * @param quizId 퀴즈 ID
     * @param user   사용자 정보
     */
    public void startQuiz(Integer quizId, User user) {
        QuizDao quiz = quizRepository.findById(quizId)
            .orElseThrow(() -> new RuntimeException("퀴즈를 찾을 수 없습니다."));

        // 로그 기록으로 대체
        LocalDateTime startTime = LocalDateTime.now();

        AnalyticsLogger.logQuizEngagement(AnalyticsLogger.QuizEngagementEvent.builder()
            .userId(user.getUserId())
            .username(user.getUsername())
            .userRank(user.getUserrank())
            .quizId(quizId)
            .quizType(quiz.getQuizSubject() != null ? quiz.getQuizSubject().name() : "REGULAR")
            .startTime(startTime)
            .endTime(null)
            .isCompleted(false)
            .durationSeconds(null)
            .build());
    }

    /**
     * 퀴즈 종료 로그 기록
     * 
     * @param quizId     퀴즈 ID
     * @param user       사용자 정보
     * @param isCompleted 퀴즈 완료 여부
     */
    public void endQuiz(Integer quizId, User user, boolean isCompleted) {
        QuizDao quiz = quizRepository.findById(quizId)
            .orElseThrow(() -> new RuntimeException("퀴즈를 찾을 수 없습니다."));

        LocalDateTime startTime = LocalDateTime.now().minusMinutes(1); // 기본 시작 시간 (1분 전)
        LocalDateTime endTime = LocalDateTime.now();

        // 시작-종료 시간 사이의 소요 시간 계산
        long durationSeconds = Duration.between(startTime, endTime).getSeconds();

        AnalyticsLogger.logQuizEngagement(AnalyticsLogger.QuizEngagementEvent.builder()
            .userId(user.getUserId())
            .username(user.getUsername())
            .userRank(user.getUserrank())
            .quizId(quizId)
            .quizType(quiz.getQuizSubject() != null ? quiz.getQuizSubject().name() : "REGULAR")
            .startTime(startTime)
            .endTime(endTime)
            .isCompleted(isCompleted)
            .durationSeconds(durationSeconds)
            .build());
    }
}