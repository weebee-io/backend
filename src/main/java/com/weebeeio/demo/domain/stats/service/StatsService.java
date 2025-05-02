package com.weebeeio.demo.domain.stats.service;

import com.weebeeio.demo.domain.stats.dao.StatsDao;
import com.weebeeio.demo.domain.stats.repository.StatsRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class StatsService {

    @Autowired
    private StatsRepository statsRepository;

    StatsService(StatsRepository statsRepository){
        this.statsRepository = statsRepository;
    }


    public Optional<StatsDao> getStatsById(Integer userid) {
        return statsRepository.findByUser_UserId(userid);
    }

    public void save(StatsDao statsDao) {
        statsRepository.save(statsDao);
    }
}
