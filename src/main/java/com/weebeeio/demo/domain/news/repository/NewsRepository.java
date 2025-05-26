package com.weebeeio.demo.domain.news.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.weebeeio.demo.domain.news.dao.NewsDao;

@Repository
public interface NewsRepository extends JpaRepository<NewsDao, Integer> {
    
}
