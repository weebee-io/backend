package com.weebeeio.demo.domain.news.service;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;


import com.weebeeio.demo.domain.news.dao.NewsDao;
import com.weebeeio.demo.domain.news.repository.NewsRepository;

import lombok.RequiredArgsConstructor;

@Service    
@RequiredArgsConstructor
public class NewsService {

    private final NewsRepository newsRepository;



    public Page<NewsDao> getNewsPage(int page, int size) {
        PageRequest pageRequest = PageRequest.of(
                page,
                size,
                Sort.by("publishedDate").descending()
        );

        return newsRepository.findAll(pageRequest);
    }
    
}
