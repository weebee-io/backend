package com.weebeeio.demo.domain.quiz.service;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.weebeeio.demo.domain.quiz.dao.QuizDao;
import com.weebeeio.demo.domain.quiz.repository.QuizRepository;

@Service
public class QuizService {

    @Autowired
    private QuizRepository quizRepository;

    QuizService(QuizRepository quizRepository) {
        this.quizRepository = quizRepository;
    }

    public Optional<QuizDao> getquiz(String subject, String level) {
        
        //throw new UnsupportedOperationException("Unimplemented method 'getquiz'");

        return quizRepository.findBySubjectAndQuizLevel(subject, level);
    }
}
