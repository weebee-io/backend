package com.weebeeio.demo.domain.news.controller;

import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;


import com.weebeeio.demo.domain.news.dao.NewsDao;
import com.weebeeio.demo.domain.news.service.NewsService;

import io.swagger.v3.oas.annotations.Operation;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/news")
@RequiredArgsConstructor
public class NewsController {

    private final NewsService newsService;

    @GetMapping
    @Operation(summary = "전체 뉴스 조회", description = "최신순으로 뉴스 조회")
    public Page<NewsDao> getNewsPage(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size
    ) {
        return newsService.getNewsPage(page, size);
    }


    @GetMapping("/{id}")
    @Operation(summary = "특정 뉴스 조회", description = "특정 뉴스를 조회")
    public Optional<NewsDao> getNewsById(@org.springframework.web.bind.annotation.PathVariable Integer id) {
        return newsService.getNewsById(id);
    }


    
}
