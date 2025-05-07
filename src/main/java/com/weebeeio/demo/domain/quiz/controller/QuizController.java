package com.weebeeio.demo.domain.quiz.controller;


import java.util.NoSuchElementException;
import java.util.Optional;

import com.weebeeio.demo.domain.stats.dao.StatsDao;
import com.weebeeio.demo.domain.stats.service.StatsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import com.weebeeio.demo.domain.quiz.dao.QuizDao;
import com.weebeeio.demo.domain.quiz.dao.QuizResultDao;

import com.weebeeio.demo.domain.quiz.service.QuizResultService;
import com.weebeeio.demo.domain.quiz.service.QuizService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;

import org.springframework.web.bind.annotation.ResponseBody;


@Tag(name = "퀴즈 API", description = "퀴즈 테스트를 진행하는 API")
@Controller("/quiz")
@RequiredArgsConstructor
public class QuizController {


    @Autowired
    private QuizService quizService;
    @Autowired
    private QuizResultService quizResultService;
    @Autowired
    private StatsService statsService;

    QuizController(QuizService quizService,QuizResultService quizResultService,StatsService statsService ) {
        this.quizService = quizService;
        this.quizResultService = quizResultService;
        this.statsService = statsService;
    }

    @ResponseBody
    @Operation(summary = "퀴즈 생성", description = "과목, 레벨로 퀴즈를 가져옵니다.")
    @GetMapping("/generation/{subject}/{level}")
    public Optional<QuizDao> getquiz(@PathVariable String subject, @PathVariable Integer level) {
        return quizService.getquiz(subject, level);
    }



    @ResponseBody
    @Operation(summary = "퀴즈 정답 확인", description = "퀴즈 정답을 확인합니다.")
    @GetMapping("/iscorrect/{user_id}/{quiz_id}/{answer}")
    public String grading(
            @PathVariable Integer user_id,
            @PathVariable Integer quiz_id,
            @PathVariable String answer) {
    
        // 1) 퀴즈와 통계(및 유저) 조회
        QuizDao quiz = quizService.findquizbyid(quiz_id)
                             .orElseThrow(() -> new NoSuchElementException("퀴즈가 없습니다."));
        StatsDao stats = statsService.getStatsById(user_id)
                             .orElseThrow(() -> new NoSuchElementException("사용자 스탯이이 없습니다.")); // StatsDao에 @ManyToOne User 매핑이 있다고 가정
    
        // 2) 기존 결과가 있으면 가져오고, 없으면 새로 생성
        QuizResultDao result = quizResultService
            .findResultbyIdandQuizid(user_id, quiz_id)
            .orElseGet(() -> {
                QuizResultDao newResult = new QuizResultDao();
                newResult.setQuizId(quiz);  // QuizDao 타입 필드 세팅                // User 엔티티 세팅
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
            return "오답";
        }
    }
    

    @ResponseBody
    @Operation(summary = "퀴즈 푼 현황", description = "유저가 현재 푼 퀴즈 현황")
    @GetMapping("/checkResult/{user_id}")
    public Optional<QuizResultDao> checkResult(@PathVariable Integer user_id) {
        return quizResultService.findResultbyid(user_id);
    }
    
    

}
