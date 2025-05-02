package com.weebeeio.demo.domain.leaderboard.repository;

import com.weebeeio.demo.domain.stats.dao.StatsDao;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface LeaderboardRepository extends JpaRepository<StatsDao, Integer> {
    @EntityGraph(attributePaths = "user")
    Page<StatsDao> findAll(Pageable pageable);
}
