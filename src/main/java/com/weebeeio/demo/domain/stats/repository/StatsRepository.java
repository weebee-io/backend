package com.weebeeio.demo.domain.stats.repository;

import com.weebeeio.demo.domain.stats.dao.StatsDao;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface StatsRepository extends JpaRepository<StatsDao, Integer> {
    
    // 한 사용자의 단일 스탯 조회 (중복 있을 경우 예외 발생)
    Optional<StatsDao> findByUser_UserId(Integer userid);
    
    // 한 사용자의 모든 스탯 조회
    List<StatsDao> findAllByUser_UserId(Integer userid);
}
