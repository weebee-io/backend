package com.weebeeio.demo.domain.quiz.service;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;


import com.weebeeio.demo.domain.quiz.dao.QuizDao;
import com.weebeeio.demo.domain.quiz.dao.QuizDao.QuizRank;
import com.weebeeio.demo.domain.quiz.dao.QuizDao.QuizSubject;
import com.weebeeio.demo.domain.quiz.repository.QuizRepository;

@Service
public class QuizService {

    @Autowired
    private QuizRepository quizRepository;

    QuizService(QuizRepository quizRepository) {
        this.quizRepository = quizRepository;
    }





    //수정정
    public List<QuizDao> getquiz(QuizSubject quizSubject, QuizRank quizRank) {
        
        //throw new UnsupportedOperationException("Unimplemented method 'getquiz'");

        return quizRepository.findByQuizSubjectAndQuizRank(quizSubject, quizRank);
    }


    public Optional<QuizDao> findquizbyid(Integer quiz_id){
        return quizRepository.findById(quiz_id);
    }





    public void save(QuizDao quiz) {
        // TODO Auto-generated method stub
        quizRepository.save(quiz);
    }


}
