package com.weebeeio.demo.global.analytics.controller;

import lombok.Data;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.MDC;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@Slf4j
@RestController
@RequestMapping("/log")
@RequiredArgsConstructor
public class AnalyticsLogController {

    @PostMapping("/news/view")
    public ResponseEntity<Void> logNewsViewed(@RequestBody NewsViewEvent event) {
        try {
            MDC.put("eventType", "NEWS_VIEWED");
            MDC.put("userId", String.valueOf(event.getUserId()));
            MDC.put("newsId", String.valueOf(event.getNewsId()));
            MDC.put("timestamp", String.valueOf(System.currentTimeMillis()));

            log.info("ANALYTICS - News Viewed: userId={}, newsId={}", event.getUserId(), event.getNewsId());
        } finally {
            MDC.clear();
        }

        return ResponseEntity.ok().build();
    }

    @PostMapping("/news/summary-clicked")
    public ResponseEntity<Void> logNewsSummaryClicked(@RequestBody NewsViewEvent event) {
        try {
            MDC.put("eventType", "NEWS_SUMMARY_CLICKED");
            MDC.put("userId", String.valueOf(event.getUserId()));
            MDC.put("newsId", String.valueOf(event.getNewsId()));
            MDC.put("timestamp", String.valueOf(System.currentTimeMillis()));

            log.info("ANALYTICS - News Summary Clicked: userId={}, newsId={}", event.getUserId(), event.getNewsId());
        } finally {
            MDC.clear();
        }

        return ResponseEntity.ok().build();
    }

    @Data
    public static class NewsViewEvent {
        private Integer userId;
        private Integer newsId;
    }
}
