package com.weebeeio.demo.domain.quiz.controller;


import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.NoSuchElementException;

import java.util.stream.Collectors;

import com.weebeeio.demo.domain.login.entity.User;
import com.weebeeio.demo.domain.quiz.dao.Quiz2Dao;
import com.weebeeio.demo.domain.quiz.dao.Quiz4Dao;
import com.weebeeio.demo.domain.quiz.dao.QuizDao;
import com.weebeeio.demo.domain.quiz.dao.QuizDao.QuizRank;
import com.weebeeio.demo.domain.quiz.dao.QuizDao.QuizSubject;
import com.weebeeio.demo.domain.quiz.repository.QuizOption2Repository;
import com.weebeeio.demo.domain.quiz.repository.QuizOption4Repository;
import com.weebeeio.demo.domain.quiz.dao.QuizResultDao;
import com.weebeeio.demo.domain.quiz.service.QuizResultService;
import com.weebeeio.demo.domain.quiz.service.QuizService;
import com.weebeeio.demo.domain.stats.dao.StatsDao;
import com.weebeeio.demo.domain.stats.service.StatsService;

import org.springframework.http.ResponseEntity;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.security.core.annotation.AuthenticationPrincipal;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
@Tag(name = "퀴즈 API", description = "퀴즈 테스트를 진행하는 API")
@RestController
@RequestMapping("/quiz")
@RequiredArgsConstructor
public class QuizController {

    private final QuizService quizService;
    private final QuizResultService quizResultService;
    private final StatsService statsService;
    private final QuizOption2Repository quizOption2Repository;
    private final QuizOption4Repository quizOption4Repository;

    @Operation(summary = "퀴즈 가져오기", description = "과목, 레벨로 퀴즈를 가져옵니다.")
    @GetMapping("/generation/{quizSubject}/{quizRank}")
    public List<QuizDao> getquiz(@PathVariable QuizSubject quizSubject, @PathVariable QuizRank quizRank) {
        return quizService.getquiz(quizSubject, quizRank);
    }


    @Operation(summary = "퀴즈 정답 확인", description = "퀴즈 정답을 확인합니다.")
    @GetMapping("/iscorrect/{quiz_id}/{answer}")
    public ResponseEntity<Map<String, Object>> grading(
            @PathVariable("quiz_id") Integer quizId,
            @PathVariable String answer) {

        // 1) 인증된 사용자
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        User user = (User) auth.getPrincipal();
        Integer userId = user.getUserId();

        // 2) 퀴즈 & 통계 조회
        QuizDao quiz = quizService.findquizbyid(quizId)
                .orElseThrow(() -> new NoSuchElementException("퀴즈가 없습니다. ID: " + quizId));
        StatsDao stats = statsService.getStatsById(userId)
                .orElseThrow(() -> new NoSuchElementException("사용자 스탯이 없습니다. ID: " + userId));
        

        // 3) 결과 엔티티 준비
        QuizResultDao result = quizResultService
            .findResultbyIdandQuizid(userId, quizId)
            .orElseGet(() -> {
                QuizResultDao r = new QuizResultDao();
                r.setQuizId(quiz);
                r.setUser(user);
                return r;
            });
            
        // 4) 정답 여부 판단 (공백, 대소문자 무시)
        boolean isCorrect = answer.equals(quiz.getCorrectAns());
        result.setIsCorrect(isCorrect);
        quizResultService.save(result);

        // 5) 통계 업데이트
        int delta = quiz.getQuizLevel();
        switch (quiz.getQuizSubject()) {
            case invest:
                stats.setInvestStat(stats.getInvestStat() + (isCorrect ? delta : -delta));
                break;
            case credit:
                stats.setCreditStat(stats.getCreditStat() + (isCorrect ? delta : -delta));
                break;
            case finance:
                stats.setFiStat(stats.getFiStat() + (isCorrect ? delta : -delta));
                break;
        }
        statsService.save(stats);

        // 6) JSON 응답 생성
        Map<String, Object> body = new HashMap<>();
        body.put("quizId", quizId);
        body.put("userId", userId);
        body.put("isCorrect", isCorrect);
        body.put("message", isCorrect ? "정답" : "오답");

        return ResponseEntity
                .ok()
                .body(body);
    }
    

    @Operation(summary = "현재까지 푼 문제 확인", description = "어떤 문제들을 시도했는지 봐요!")
    @GetMapping("/checkResult")
    public ResponseEntity<List<QuizResultDao>> checkResult(
            @AuthenticationPrincipal User user /* 또는 UserDetails */) {
        Integer userId = user.getUserId();
        List<QuizResultDao> results = quizResultService.findAllByUserId(userId);
        return ResponseEntity.ok(results);
    }
    
    
    @Operation(summary = "퀴즈 일괄 업로드", description = "텍스트 파일을 업로드하여 퀴즈를 일괄 등록합니다.")
    @PostMapping(path = "/admin/upload", consumes = "multipart/form-data")
    public ResponseEntity<String> uploadQuizFile(@RequestParam("file") MultipartFile file) {
        if (file.isEmpty()) {
            return ResponseEntity.badRequest().body("파일이 비어 있습니다.");
        }

        int count = 0;
        try (BufferedReader reader = new BufferedReader(
                new InputStreamReader(file.getInputStream(), StandardCharsets.UTF_8))) {

            // 세미콜론(;)으로 엔트리 구분
            String content = reader.lines().collect(Collectors.joining(""));
            String[] entries = content.split(";");

            for (String entry : entries) {
                if (entry == null || entry.isBlank()) continue;
                String[] parts = entry.split(",");

                // 최소 7개(2지선다), 최대 9개(4지선다) 확인
                if (parts.length != 7 && parts.length != 9) continue;

                String quizContent = parts[0].trim();
                int level         = Integer.parseInt(parts[1].trim());
                String subject    = parts[2].trim();
                QuizSubject subjectEnum = QuizSubject.valueOf(subject);
                QuizRank rank     = QuizRank.valueOf(parts[3].trim().toUpperCase());

                // 1) Quiz 저장
                QuizDao quiz = QuizDao.builder()
                        .quizContent(quizContent)
                        .quizLevel(level)
                        .quizSubject(subjectEnum)
                        .quizRank(rank)
                        .build();
                quizService.save(quiz);

                // 2) 옵션 저장
                if (parts.length == 7) {
                    // 2지선다 (BRONZE, SILVER)
                    Quiz2Dao option2 = Quiz2Dao.builder()
                        .quiz(quiz)
                        .choiceA(parts[4].trim())
                        .choiceB(parts[5].trim())
                        .correctAns(parts[6].trim())
                        .build();
                    quizOption2Repository.save(option2);

                } else {
                    // 4지선다 (GOLD)
                    Quiz4Dao option4 = Quiz4Dao.builder()
                        .quiz(quiz)
                        .choiceA(parts[4].trim())
                        .choiceB(parts[5].trim())
                        .choiceC(parts[6].trim())
                        .choiceD(parts[7].trim())
                        .correctAns(parts[8].trim())
                        .build();
                    quizOption4Repository.save(option4);
                }

                count++;
            }

        } catch (Exception e) {
            return ResponseEntity
                    .badRequest()
                    .body("파일 처리 중 오류가 발생했습니다: " + e.getMessage());
        }

        return ResponseEntity.ok("총 " + count + "개의 퀴즈가 등록되었습니다.");
    }

}
