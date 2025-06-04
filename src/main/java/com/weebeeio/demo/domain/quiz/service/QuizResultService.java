package com.weebeeio.demo.domain.quiz.service;

import com.weebeeio.demo.domain.quiz.dao.QuizResultDao;

import com.weebeeio.demo.domain.quiz.repository.QuizResultRepository;

import java.util.List;
import java.util.Optional;
import java.util.Map;
import java.util.HashMap;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class QuizResultService {

    @Autowired
    private QuizResultRepository quizResultRepository;

    QuizResultService(QuizResultRepository quizResultRepository) {
        this.quizResultRepository = quizResultRepository;
    }

    
    public Optional<QuizResultDao> findResultbyIdandQuizid(Integer user_id,Integer quiz_id){
        return quizResultRepository.findByUserUserIdAndQuizIdQuizId(user_id,quiz_id);
    }



    public Optional<QuizResultDao> findResultbyid(Integer user_id) {

        return quizResultRepository.findById(user_id);
    }


    public void save(QuizResultDao quizResult) {
        quizResultRepository.save(quizResult);
        
    }


    public List<QuizResultDao> getResultById(Integer user_id) {
        return quizResultRepository.findByUser_UserId(user_id);
    }



    public List<QuizResultDao> findAllByUserId(Integer userId) {
        return quizResultRepository.findAllByUser_UserId(userId);
    }

    public Map<String, Object> getQuizCorrectRate(Integer quizId) {
        Long totalAttempts = quizResultRepository.countTotalAttemptsByQuizId(quizId);
        Long correctAttempts = quizResultRepository.countCorrectAttemptsByQuizId(quizId);
        
        double correctRate = totalAttempts > 0 ? 
            (double) correctAttempts / totalAttempts * 100 : 0.0;
        
        Map<String, Object> result = new HashMap<>();
        result.put("quizId", quizId);
        result.put("totalAttempts", totalAttempts);
        result.put("correctAttempts", correctAttempts);
        result.put("correctRate", Math.round(correctRate * 100.0) / 100.0); // 소수점 2자리까지
        
        return result;
    }

}
