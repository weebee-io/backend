package com.weebeeio.demo.global.analytics.controller;

import lombok.RequiredArgsConstructor;
import org.slf4j.MDC;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import com.weebeeio.demo.global.analytics.AnalyticsLogger;
import com.weebeeio.demo.global.analytics.dto.EventDto;

import java.util.Map;

@RestController
@RequestMapping("/api/analytics/events")
@RequiredArgsConstructor
@Tag(name = "Analytics", description = "사용자 행동 분석 API")
public class AnalyticsController {

    @PostMapping
    @Operation(
        summary = "통합 이벤트 로깅",
        description = "다양한 사용자 행동 이벤트를 단일 엔드포인트로 수집 (인증 불필요)",
        responses = {
            @ApiResponse(responseCode = "200", description = "이벤트 로깅 성공"),
            @ApiResponse(responseCode = "400", description = "잘못된 요청 형식")
        }
    )
    public ResponseEntity<Void> logUnifiedEvent(@RequestBody EventDto eventDto) {
        try {
            // 기본 필드들을 MDC에 추가
            MDC.put("eventType", eventDto.getEventType());
            MDC.put("userId", String.valueOf(eventDto.getUserId()));
            MDC.put("username", eventDto.getUsername());
            MDC.put("timestamp", eventDto.getTimestamp().toString());

            // properties의 모든 키-값을 MDC에 추가
            if (eventDto.getProperties() != null) {
                Map<String, String> mdcProperties = eventDto.getProperties().entrySet().stream()
                    .collect(java.util.stream.Collectors.toMap(
                        Map.Entry::getKey,
                        e -> String.valueOf(e.getValue())
                    ));
                mdcProperties.forEach(MDC::put);
            }

            // 이벤트 타입에 따라 적절한 로깅 메서드 호출
            switch (eventDto.getEventType()) {
                case "newsViewed":
                    AnalyticsLogger.logNewsViewed(
                        AnalyticsLogger.NewsViewedEvent.builder()
                            .userId(eventDto.getUserId())
                            .username(eventDto.getUsername())
                            .newsId((Integer) eventDto.getProperties().get("newsId"))
                            .experimentNewsLayout((String) eventDto.getProperties().get("experimentNewsLayout"))
                            .build()
                    );
                    break;

                case "newsSummaryClicked":
                    AnalyticsLogger.logNewsSummaryClicked(
                        AnalyticsLogger.NewsSummaryClickedEvent.builder()
                            .userId(eventDto.getUserId())
                            .username(eventDto.getUsername())
                            .newsId((Integer) eventDto.getProperties().get("newsId"))
                            .summaryLength((Integer) eventDto.getProperties().get("summaryLength"))
                            .summaryStyle((String) eventDto.getProperties().get("summaryStyle"))
                            .build()
                    );
                    break;

                case "quizStarted":
                    AnalyticsLogger.logQuizEngagement(
                        AnalyticsLogger.QuizEngagementEvent.builder()
                            .userId(eventDto.getUserId())
                            .username(eventDto.getUsername())
                            .quizId((Integer) eventDto.getProperties().get("quizId"))
                            .quizType((String) eventDto.getProperties().get("quizType"))
                            .startTime(eventDto.getTimestamp())
                            .build()
                    );
                    break;

                case "quizEnded":
                    AnalyticsLogger.logQuizResult(
                        AnalyticsLogger.QuizResultEvent.builder()
                            .userId(eventDto.getUserId())
                            .username(eventDto.getUsername())
                            .quizId((Integer) eventDto.getProperties().get("quizId"))
                            .quizType((String) eventDto.getProperties().get("quizType"))
                            .isCorrect((Boolean) eventDto.getProperties().get("isCorrect"))
                            .difficulty((Integer) eventDto.getProperties().get("difficulty"))
                            .category((String) eventDto.getProperties().get("category"))
                            .responseTimeMs((Long) eventDto.getProperties().get("responseTimeMs"))
                            .build()
                    );
                    break;

                default:
                    // 알 수 없는 이벤트 타입은 일반 로깅
                    AnalyticsLogger.logEvent(eventDto);
            }

            return ResponseEntity.ok().build();
        } finally {
            MDC.clear();
        }
    }
} 