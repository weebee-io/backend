package com.weebeeio.demo.domain.newsQuiz.service;

import java.util.Optional;

import org.springframework.stereotype.Service;

import com.weebeeio.demo.domain.newsQuiz.dao.NewsQuizResultDao;
import com.weebeeio.demo.domain.newsQuiz.repository.NewsQuizResultRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class NewsQuizResultService {
    
    private final NewsQuizResultRepository newsQuizResultRepository;
    


    public Optional<NewsQuizResultDao> findResultbyIdandQuizid(Integer userId, Integer newsQuizId) {
        return newsQuizResultRepository.findByIdandQuizid(userId, newsQuizId);
    }

    public void save(NewsQuizResultDao newsQuizResultDao) {
        newsQuizResultRepository.save(newsQuizResultDao);
    }
}
