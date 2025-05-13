package com.weebeeio.demo.domain.quiz.controller;


import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;
import java.util.stream.Collectors;

import com.weebeeio.demo.domain.login.entity.User;
import com.weebeeio.demo.domain.quiz.dao.QuizDao;
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


    @Operation(summary = "퀴즈 가져오기", description = "과목, 레벨로 퀴즈를 가져옵니다.")
    @GetMapping("/generation/{subject}/{level}")
    public Optional<QuizDao> getquiz(@PathVariable String subject, @PathVariable Integer level) {
        return quizService.getquiz(subject, level);
    }


    @Operation(summary = "퀴즈 정답 확인", description = "퀴즈 정답을 확인합니다.")
    @GetMapping("/iscorrect/{quiz_id}/{answer}")
    public String grading(
            @PathVariable Integer quiz_id,
            @PathVariable String answer) {
    
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        User user = (User) auth.getPrincipal(); 
        Integer user_id = user.getUserId();       


        // 1) 퀴즈와 통계(및 유저) 조회ㄴ
        QuizDao quiz = quizService.findquizbyid(quiz_id)
                             .orElseThrow(() -> new NoSuchElementException("퀴즈가 없습니다."));
        StatsDao stats = statsService.getStatsById(user_id)
                             .orElseThrow(() -> new NoSuchElementException("사용자 스탯이 없습니다.")); // StatsDao에 @ManyToOne User 매핑이 있다고 가정
    
        // 2) 기존 결과가 있으면 가져오고, 없으면 새로 생성
        QuizResultDao result = quizResultService
            .findResultbyIdandQuizid(user_id, quiz_id)
            .orElseGet(() -> {
                QuizResultDao newResult = new QuizResultDao();
                newResult.setQuizId(quiz);  // QuizDao 타입 필드 세팅                // User 엔티티 세팅
                newResult.setUser(user);
                return newResult;
            });
    
        // 3) 정답 여부 판단 및 저장
        boolean isCorrect = answer.equals(quiz.getQuizAnswer());
        result.setIsCorrect(isCorrect);
        quizResultService.save(result);
    
        // 4) 정답일 때만 통계 업데이트
        if (isCorrect) {
            switch (quiz.getSubject()) {
                case "재태크":
                    stats.setInvestStat(stats.getInvestStat() + quiz.getQuizLevel());
                    break;
                case "신용/소비":
                    stats.setCreditStat(stats.getCreditStat() + quiz.getQuizLevel());
                    break;
                case "금융상식":
                    stats.setFiStat(stats.getFiStat() + quiz.getQuizLevel());
                    break;
            }
            statsService.save(stats);
            return "정답";
        } else {
            switch (quiz.getSubject()) {
                case "재태크":
                    stats.setInvestStat(stats.getInvestStat() - quiz.getQuizLevel());
                    break;
                case "신용/소비":
                    stats.setCreditStat(stats.getCreditStat() - quiz.getQuizLevel());
                    break;
                case "금융상식":
                    stats.setFiStat(stats.getFiStat() - quiz.getQuizLevel());
                    break;
            }
            statsService.save(stats);
            return "오답";
        }
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
    public ResponseEntity<String> uploadQuizFile(
            @RequestParam("file") MultipartFile file) {
        if (file.isEmpty()) {
            return ResponseEntity.badRequest().body("파일이 비어 있습니다.");
        }
        int count = 0;
        try (BufferedReader reader = new BufferedReader(
                new InputStreamReader(file.getInputStream(), StandardCharsets.UTF_8))) {
            String content = reader.lines().collect(Collectors.joining(""));
            String[] entries = content.split(";");
            for (String entry : entries) {
                if (entry == null || entry.isBlank()) continue;
                String[] parts = entry.split(",");
                if (parts.length < 4) continue;
                String quizContent = parts[0].trim();
                String answer = parts[1].trim();
                int level = Integer.parseInt(parts[2].trim());
                String subject = parts[3].trim();

                QuizDao quiz = new QuizDao();
                quiz.setQuizcontent(quizContent);
                quiz.setQuizAnswer(answer);
                quiz.setQuizLevel(level);
                quiz.setSubject(subject);
                quizService.save(quiz);
                count++;
            }
        } catch (IOException | NumberFormatException e) {
            return ResponseEntity.badRequest().body("파일 파싱 중 오류가 발생했습니다: " + e.getMessage());
        }
        return ResponseEntity.ok("총 " + count + "개의 퀴즈가 등록되었습니다.");
    }

}
