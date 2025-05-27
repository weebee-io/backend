package com.weebeeio.demo.domain.quiz.controller;


import java.io.BufferedReader;
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
import com.weebeeio.demo.domain.quiz.dto.PlacementTestdto;
import com.weebeeio.demo.domain.quiz.service.QuizResultService;
import com.weebeeio.demo.domain.quiz.service.QuizService;
import com.weebeeio.demo.domain.stats.dao.StatsDao;
import com.weebeeio.demo.domain.stats.service.StatsService;
import com.weebeeio.demo.domain.stats.service.LuckService;
import com.weebeeio.demo.domain.login.service.UserService;

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
    private final LuckService luckService;
    private final UserService userService;
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
                .orElseThrow(() -> new NoSuchElementException("퀘즈가 없습니다. ID: " + quizId));
        
        // 통계 데이터 가져오기 - 개선된 메서드 사용
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

        // 5) 운 스탯 보너스 적용
        int delta = quiz.getQuizLevel();
        Integer luckStat = stats.getLuckStat();
        if (luckStat == null) {
            luckStat = 0;
            stats.setLuckStat(0);
        }
        
        // 정답일 경우에만 운 스탯 보너스 적용
        if (isCorrect) {
            delta = luckService.applyLuckBonus(delta, luckStat);
        }
        
        // 통계 업데이트
        switch (quiz.getQuizSubject()) {
            case invest:
                if (isCorrect) {
                    stats.setInvestStat(stats.getInvestStat() + delta);
                } else {
                    int newValue = Math.max(0, stats.getInvestStat() - delta);
                    stats.setInvestStat(newValue);
                }
                break;
            case credit:
                if (isCorrect) {
                    stats.setCreditStat(stats.getCreditStat() + delta);
                } else {
                    int newValue = Math.max(0, stats.getCreditStat() - delta);
                    stats.setCreditStat(newValue);
                }
                break;
            case finance:
                if (isCorrect) {
                    stats.setFiStat(stats.getFiStat() + delta);
                } else {
                    int newValue = Math.max(0, stats.getFiStat() - delta);
                    stats.setFiStat(newValue);
                }
                break;
        }
        statsService.save(stats);

        // 6) JSON 응답 생성
        Map<String, Object> body = new HashMap<>();
        body.put("quizId", quizId);
        body.put("userId", userId);
        body.put("isCorrect", isCorrect);
        body.put("message", isCorrect ? "정답" : "오답");
        
        // 운 스탯 관련 정보 추가
        if (isCorrect) {
            body.put("luckStat", luckStat);
            body.put("originalPoints", quiz.getQuizLevel());
            body.put("bonusPoints", delta - quiz.getQuizLevel());
            body.put("totalPoints", delta);
        }

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

    @Operation(summary = "배치고사 성적 받기", description = "배치고사 성적을 JSON 형태로 받아 처리후 배정된 랭크를 반환")
    @PostMapping("/placementTest")
    public ResponseEntity<Map<String, Object>> getPlacementTest(
            @RequestBody PlacementTestdto placementTestdto,
            @AuthenticationPrincipal User user) {
        
        Integer userId = user.getUserId();
        
        Integer investStat = placementTestdto.getInvestStat();
        Integer creditStat = placementTestdto.getCreditStat();
        Integer fiStat = placementTestdto.getFiStat();

        StatsDao stats = statsService.getStatsById(userId)
                .orElseThrow(() -> new NoSuchElementException("사용자 스탯이 없습니다. ID: " + userId));

        if (stats.getInvestStat() + investStat >= 0) {
            stats.setInvestStat(stats.getInvestStat() + investStat);
        }
        else{
            stats.setInvestStat(0);
        }
        
        if (stats.getCreditStat() + creditStat >= 0) {
            stats.setCreditStat(stats.getCreditStat() + creditStat);
        }
        else{
            stats.setCreditStat(0);
        }
        
        if (stats.getFiStat() + fiStat >= 0) {
            stats.setFiStat(stats.getFiStat() + fiStat);
        }
        else{
            stats.setFiStat(0);
        }
        
        statsService.save(stats);

        User updatedUser = userService.getUserInfo(userId)
                .orElseThrow(() -> new NoSuchElementException("사용자 정보를 찾을 수 없습니다. ID: " + userId));
        
        Map<String, Object> response = new HashMap<>();
        response.put("nickname", updatedUser.getNickname());
        response.put("investStat", stats.getInvestStat());
        response.put("creditStat", stats.getCreditStat());
        response.put("fiStat", stats.getFiStat());
        response.put("rank", updatedUser.getUserrank());
    
        
        return ResponseEntity.ok(response);
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
