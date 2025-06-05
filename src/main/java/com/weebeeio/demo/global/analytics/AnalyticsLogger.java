package com.weebeeio.demo.global.analytics;

import lombok.Builder;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.MDC;

import java.time.LocalDateTime;

/**
 * 분석을 위한 구조화된 로그를 생성하는 클래스
 * ELK 스택을 통한 분석에 최적화된 형태로 로깅
 */
@Slf4j
public class AnalyticsLogger {

    /**
     * 퀴즈 결과를 로깅하는 메서드
     * @param event 퀴즈 결과 이벤트 객체
     */
    public static void logQuizResult(QuizResultEvent event) {
        try {
            // MDC에 모든 필드 추가 - Kibana에서 검색 및 필터링 가능하도록
            MDC.put("eventType", "QUIZ_RESULT");
            MDC.put("userId", String.valueOf(event.getUserId()));
            MDC.put("username", event.getUsername());
            MDC.put("userRank", event.getUserRank() != null ? event.getUserRank() : "NONE");
            MDC.put("quizId", String.valueOf(event.getQuizId()));
            MDC.put("quizType", event.getQuizType());
            MDC.put("isCorrect", String.valueOf(event.isCorrect()));
            MDC.put("difficulty", String.valueOf(event.getDifficulty()));
            MDC.put("category", event.getCategory() != null ? event.getCategory() : "UNKNOWN");
            MDC.put("responseTimeMs", String.valueOf(event.getResponseTimeMs()));
            MDC.put("statCategory", event.getStatCategory());
            MDC.put("statBefore", String.valueOf(event.getStatBefore()));
            MDC.put("statAfter", String.valueOf(event.getStatAfter()));
            MDC.put("statDelta", String.valueOf(event.getStatAfter() - event.getStatBefore()));
            
            // 로그 메시지 - 구조화된 데이터는 MDC로, 주요 정보는 메시지에도 포함
            log.info("ANALYTICS - Quiz Result: userId={}, quizType={}, category={}, isCorrect={}, difficulty={}", 
                event.getUserId(), event.getQuizType(), event.getCategory(), event.isCorrect(), event.getDifficulty());
            
        } finally {
            MDC.clear(); // MDC 정리는 필수
        }
    }
    
    /**
     * 뉴스 퀴즈 결과를 로깅하는 메서드
     * @param event 뉴스 퀴즈 결과 이벤트 객체
     */
    public static void logNewsQuizResult(NewsQuizResultEvent event) {
        try {
            // MDC에 모든 필드 추가
            MDC.put("eventType", "NEWS_QUIZ_RESULT");
            MDC.put("userId", String.valueOf(event.getUserId()));
            MDC.put("username", event.getUsername());
            MDC.put("userRank", event.getUserRank() != null ? event.getUserRank() : "NONE");
            MDC.put("newsQuizId", String.valueOf(event.getNewsQuizId()));
            MDC.put("newsTitle", event.getNewsTitle());
            MDC.put("isCorrect", String.valueOf(event.isCorrect()));
            MDC.put("difficulty", String.valueOf(event.getDifficulty()));
            MDC.put("category", event.getCategory());
            MDC.put("responseTimeMs", String.valueOf(event.getResponseTimeMs()));
            MDC.put("newsStatBefore", String.valueOf(event.getNewsStatBefore()));
            MDC.put("newsStatAfter", String.valueOf(event.getNewsStatAfter()));
            MDC.put("statDelta", String.valueOf(event.getNewsStatAfter() - event.getNewsStatBefore()));
            
            log.info("ANALYTICS - News Quiz Result: userId={}, newsQuizId={}, category={}, isCorrect={}, difficulty={}", 
                event.getUserId(), event.getNewsQuizId(), event.getCategory(), event.isCorrect(), event.getDifficulty());
            
        } finally {
            MDC.clear();
        }
    }

    /**
     * 퀴즈 결과 이벤트 데이터 클래스
     */
    @Data
    @Builder
    public static class QuizResultEvent {
        private Integer userId;
        private String username;
        private String userRank;
        private Integer quizId;
        private String quizType; // "REGULAR", "FINANCE", "INVESTMENT" 등
        private boolean isCorrect;
        private Integer difficulty;
        private String category;
        private Long responseTimeMs; // 응답 시간
        private String statCategory; // "fiStat", "investStat" 등
        private Integer statBefore;
        private Integer statAfter;
    }
    
    /**
     * 뉴스 퀴즈 결과 이벤트 데이터 클래스
     */
    @Data
    @Builder
    public static class NewsQuizResultEvent {
        private Integer userId;
        private String username;
        private String userRank;
        private Integer newsQuizId;
        private String newsTitle;
        private boolean isCorrect;
        private Integer difficulty;
        private String category;
        private Long responseTimeMs; // 응답 시간
        private Integer newsStatBefore;
        private Integer newsStatAfter;
    }

    /**
     * 퀴즈 참여를 로깅하는 메서드
     * @param event 퀴즈 참여 이벤트 객체
     */
    public static void logQuizEngagement(QuizEngagementEvent event) {
        try {
            // MDC에 모든 필드 추가 - Kibana에서 검색 및 필터링 가능하도록
            MDC.put("eventType", "QUIZ_ENGAGEMENT");
            MDC.put("engagementId", String.valueOf(event.getEngagementId()));
            MDC.put("userId", String.valueOf(event.getUserId()));
            MDC.put("username", event.getUsername());
            MDC.put("userRank", event.getUserRank() != null ? event.getUserRank() : "NONE");
            MDC.put("quizId", String.valueOf(event.getQuizId()));
            MDC.put("quizType", event.getQuizType() != null ? event.getQuizType() : "UNKNOWN");
            MDC.put("startTime", event.getStartTime().toString());
            MDC.put("endTime", event.getEndTime() != null ? event.getEndTime().toString() : "NONE");
            MDC.put("isCompleted", String.valueOf(event.isCompleted()));
            MDC.put("durationSeconds", String.valueOf(event.getDurationSeconds()));
            
            // 로그 메시지 - 구조화된 데이터는 MDC로, 주요 정보는 메시지에도 포함
            log.info("ANALYTICS - Quiz Engagement: userId={}, quizId={}, quizType={}, isCompleted={}, durationSeconds={}",
                event.getUserId(), event.getQuizId(), event.getQuizType(), event.isCompleted(), event.getDurationSeconds());
            
        } finally {
            MDC.clear(); // MDC 정리는 필수
        }
    }
    
    /**
     * 퀴즈 참여 이벤트 데이터 클래스
     */
    @Data
    @Builder
    public static class QuizEngagementEvent {
        private Integer engagementId;
        private Integer userId;
        private String username;
        private String userRank;
        private Integer quizId;
        private String quizType;
        private LocalDateTime startTime;
        private LocalDateTime endTime;
        private boolean isCompleted;
        private Long durationSeconds; // 진행 시간(초)
    }
}
