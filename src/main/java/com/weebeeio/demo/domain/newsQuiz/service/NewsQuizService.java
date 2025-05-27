package com.weebeeio.demo.domain.newsQuiz.service;

import com.weebeeio.demo.domain.newsQuiz.dao.NewsQuizDao;
import com.weebeeio.demo.domain.newsQuiz.repository.NewsQuizRepository;

import lombok.AllArgsConstructor;

import java.util.Optional;

import org.springframework.stereotype.Service;

@AllArgsConstructor
@Service    
public class NewsQuizService {
    
 
    private final NewsQuizRepository newsQuizRepository;
    
    public Optional<NewsQuizDao> getNewsQuizById(Integer newsQuizId) {
        return newsQuizRepository.findById(newsQuizId);
    }
}
