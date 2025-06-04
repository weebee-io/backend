package com.weebeeio.demo.domain.newsQuiz.controller;

import java.util.HashMap;
import java.util.Map;
import java.util.NoSuchElementException;

import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.weebeeio.demo.domain.stats.dao.StatsDao;
import com.weebeeio.demo.domain.stats.service.LuckService;  
import com.weebeeio.demo.domain.stats.service.StatsService;
import com.weebeeio.demo.domain.login.entity.User;
import com.weebeeio.demo.domain.newsQuiz.dao.NewsQuizDao;
import com.weebeeio.demo.domain.newsQuiz.service.NewsQuizResultService;
import com.weebeeio.demo.domain.newsQuiz.service.NewsQuizService;
import com.weebeeio.demo.domain.newsQuiz.dao.NewsQuizResultDao;
import com.weebeeio.demo.global.analytics.AnalyticsLogger;
import com.weebeeio.demo.global.analytics.AnalyticsLogger.NewsQuizResultEvent;


import org.springframework.security.core.Authentication;

import io.swagger.v3.oas.annotations.Operation;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/newsQuiz")
@RequiredArgsConstructor
public class NewsQuizController {

    private final NewsQuizService newsQuizService;
    private final StatsService statsService;
    private final LuckService luckService;
    private final NewsQuizResultService newsQuizResultService;

    @GetMapping("/iscorrect/{newsQuizId}/{answer}")
    @Operation(summary = "뉴스 퀴즈 정답확인", description = "뉴스 퀴즈 정답을 확인합니다.")
    public ResponseEntity<Map<String, Object>> gradingNewsQuiz(
            @PathVariable Integer newsQuizId,
            @PathVariable String answer
    ) {
        // 1) 인증된 사용자
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        User user = (User) auth.getPrincipal();
        Integer userId = user.getUserId();

        StatsDao stats = statsService.getStatsById(userId)
                .orElseThrow(() -> new NoSuchElementException("사용자 스탯이 없습니다. ID: " + userId));
        
        if(stats.getNewsStat() == null) {
            stats.setNewsStat(0);
        }

        // 2) 퀴즈 & 통계 조회
        NewsQuizDao quiz = newsQuizService.getNewsQuizById(newsQuizId)
                .orElseThrow(() -> new NoSuchElementException("퀘즈가 없습니다. ID: " + newsQuizId));

        // 3) 뉴스 퀴즈에 맞게 간소화된 결과 처리
        // 뉴스 퀴즈는 QuizDao와 구조가 달라서 별도 처리 필요
                // 3) 결과 엔티티 준비
        NewsQuizResultDao result = newsQuizResultService
            .findResultbyIdandQuizid(userId, newsQuizId)
            .orElseGet(() -> {
                NewsQuizResultDao r = new NewsQuizResultDao();
                r.setNewsquizId(quiz);
                r.setUser(user);
                return r;
            });
            
        
        // 4) 정답 여부 판단 (공백, 대소문자 무시)
        boolean isCorrect = answer.equals(quiz.getNewsquizCorrectAns());
        result.setIsCorrect(isCorrect);
        newsQuizResultService.save(result);
        
        // 5) 보상 계산 - 뉴스 퀴즈에서는 고정 점수 사용
        int delta = quiz.getNewsquizScore(); // 뉴스 퀴즈의 점수 필드 사용
        Integer luckStat = stats.getLuckStat();
        if (luckStat == null) {
            luckStat = 0;
            stats.setLuckStat(0);
        }
        
        // 정답일 경우에만 운 스탯 보너스 적용
        if (isCorrect) {
            delta = luckService.applyLuckBonus(delta, luckStat);
            
            // 통계 업데이트 - 뉴스 퀴즈는 금융 관련이므로 finance 스탯 올림
            stats.setNewsStat(stats.getNewsStat() + delta);
        } else {
            stats.setNewsStat(stats.getNewsStat() - delta); 
        }
        
        statsService.save(stats);
        
        // 5-1) 분석용 로그 기록
        // 클라이언트 응답 시간이 없으므로 NewsQuizResultDao의 newsquizDate를 기준으로 추정
        long responseTimeMs = 0; // 응답 시간은 클라이언트 사이드에서 보내주는 것이 필요하지만 일단 0으로 설정
        
        // 뉴스퀴즈 결과 로깅
        AnalyticsLogger.logNewsQuizResult(
            NewsQuizResultEvent.builder()
                .userId(userId)
                .username(user.getUsername())
                .userRank("NONE") // 사용자 랜크 구현 필요
                .newsQuizId(newsQuizId)
                .newsTitle(quiz.getNewsquizContent()) // 뉴스 타이틀 대신 퀴즈 내용 사용
                .isCorrect(isCorrect)
                .category("NEWS") // 고정 카테고리 사용
                .responseTimeMs(responseTimeMs)
                .newsStatBefore(stats.getNewsStat() - (isCorrect ? delta : -delta))
                .newsStatAfter(stats.getNewsStat())
                .build()
        );

        // 6) JSON 응답 생성
        Map<String, Object> body = new HashMap<>();
        body.put("newsQuizId", newsQuizId);
        body.put("userId", userId);
        body.put("isCorrect", isCorrect);
        body.put("message", isCorrect ? "정답" : "오답");
        
        // 운 스탯 관련 정보 추가
        if (isCorrect) {
            body.put("luckStat", luckStat);
            body.put("originalPoints", quiz.getNewsquizScore());
            body.put("bonusPoints", delta - quiz.getNewsquizScore());
            body.put("totalPoints", delta);
        }

        return ResponseEntity
                .ok()
                .body(body);
        
    }
    
    
}
