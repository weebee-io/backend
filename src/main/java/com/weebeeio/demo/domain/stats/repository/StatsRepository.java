package com.weebeeio.demo.domain.stats.repository;

import com.weebeeio.demo.domain.stats.dao.StatsDao;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;


@Repository
public interface StatsRepository extends JpaRepository<StatsDao, Integer> {
    // 가장 최근(스탯 ID가 가장 큰) 레코드를 가져오도록 수정
    Optional<StatsDao> findTopByUser_UserIdOrderByStatsIdDesc(Integer userid);
}
