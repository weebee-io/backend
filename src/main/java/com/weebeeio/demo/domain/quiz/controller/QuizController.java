package com.weebeeio.demo.domain.quiz.controller;


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

import org.springframework.web.bind.annotation.ResponseBody;


@Tag(name = "퀴즈 API", description = "퀴즈 테스트를 진행하는 API")
@Controller("/quiz")
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
    public String grading(@PathVariable Integer user_id,@PathVariable Integer quiz_id, @PathVariable String answer) {
        
        Optional<QuizDao> quiz = quizService.findquizbyid(quiz_id);
        Optional<QuizResultDao> quizResult = quizResultService.findResultbyIdandQuizid(user_id,quiz_id);
        Optional<StatsDao> stats = statsService.getStatsById(user_id);

        String quizcorrect = quiz.get().getQuizAnswer();

        if (answer.equals(quizcorrect)){
            boolean iscorrect = true;
            quizResult.get().setIsCorrect(iscorrect);
            String subject = quiz.get().getSubject();
            switch (subject) {
                case "재태크":
                   stats.get().setInvestStat(stats.get().getInvestStat()+quiz.get().getQuizLevel());
                    break;
                case "신용/소비":
                    stats.get().setCreditStat(stats.get().getCreditStat()+quiz.get().getQuizLevel());
                    break;
                case "금융상식":
                    stats.get().setFiStat(stats.get().getFiStat()+quiz.get().getQuizLevel());
                    break;
            }
            quizResultService.save(quizResult);
            statsService.save(stats.get());
            return "정답";
        }
        return "오답";
        
    }


    @ResponseBody
    @Operation(summary = "퀴즈 푼 현황", description = "유저가 현재 푼 퀴즈 현황")
    @GetMapping("/checkResult/{user_id}")
    public Optional<QuizResultDao> checkResult(@PathVariable Integer user_id) {
        return quizResultService.findResultbyid(user_id);
    }
    



}
