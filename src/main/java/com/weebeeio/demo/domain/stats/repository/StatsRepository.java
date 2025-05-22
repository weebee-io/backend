package com.weebeeio.demo.domain.stats.repository;

import com.weebeeio.demo.domain.stats.dao.StatsDao;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;


import java.util.Optional;


@Repository
public interface StatsRepository extends JpaRepository<StatsDao, Integer> {
    
    Optional<StatsDao> findByUser_UserId(Integer userid);
}
