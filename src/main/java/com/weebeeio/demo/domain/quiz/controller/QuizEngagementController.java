package com.weebeeio.demo.domain.quiz.controller;

import com.weebeeio.demo.domain.quiz.service.QuizEngagementService;
import com.weebeeio.demo.domain.login.entity.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/quiz/log")
public class QuizEngagementController {
    @Autowired
    private QuizEngagementService quizEngagementService;

    public QuizEngagementController(QuizEngagementService quizEngagementService) {
        this.quizEngagementService = quizEngagementService;
    }

    @PostMapping("/start/{quizId}")
    public ResponseEntity<Map<String, Object>> startQuiz(
        @PathVariable Integer quizId,
        @AuthenticationPrincipal User user
    ) {
        try {
            quizEngagementService.startQuiz(quizId, user);
            Map<String, Object> response = new HashMap<>();
            response.put("success", true);
            response.put("message", "퀴즈 시작 로그가 기록되었습니다.");
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            Map<String, Object> response = new HashMap<>();
            response.put("success", false);
            response.put("error", e.getMessage());
            return ResponseEntity.badRequest().body(response);
        }
    }

    @PostMapping("/end/{quizId}")
    public ResponseEntity<Map<String, Object>> endQuiz(
        @PathVariable Integer quizId,
        @RequestBody Map<String, Object> body,
        @AuthenticationPrincipal User user
    ) {
        try {
            boolean isCompleted = body.containsKey("isCompleted") ? 
                (Boolean) body.get("isCompleted") : false;
            
            quizEngagementService.endQuiz(quizId, user, isCompleted);
            
            Map<String, Object> response = new HashMap<>();
            response.put("success", true);
            response.put("message", "퀴즈 종료 로그가 기록되었습니다.");
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            Map<String, Object> response = new HashMap<>();
            response.put("success", false);
            response.put("error", e.getMessage());
            return ResponseEntity.badRequest().body(response);
        }
    }
} 