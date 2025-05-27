package com.weebeeio.demo.domain.news.repository;

import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.weebeeio.demo.domain.news.dao.NewsDao;

@Repository
public interface NewsRepository extends JpaRepository<NewsDao, Integer> {
    
    //@EntityGraph(attributePaths = "user")
    Page<NewsDao> findAll(Pageable pageable);

    Optional<NewsDao> findById(Integer id);
}
