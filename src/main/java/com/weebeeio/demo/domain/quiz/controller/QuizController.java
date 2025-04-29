package com.weebeeio.demo.domain.quiz.controller;


import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import com.weebeeio.demo.domain.quiz.dao.QuizDao;
import com.weebeeio.demo.domain.quiz.service.QuizService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;

@Tag(name = "퀴즈 API 테스트", description = "퀴즈 테스트를 진행하는 API")
@Controller("/quiz")
public class QuizController {

    @Autowired
    private QuizService quizService;

    @Operation(summary = "퀴즈 생성", description = "과목, 레벨로 퀴즈를 가져옵니다.")
    @GetMapping("/generation/{subject}/{level}")
    public Optional<QuizDao> getquiz(@PathVariable String subject, @PathVariable String level) {
        return quizService.getquiz(subject, level);
    }


}
