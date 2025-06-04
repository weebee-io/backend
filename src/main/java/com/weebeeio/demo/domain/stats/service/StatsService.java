package com.weebeeio.demo.domain.stats.service;

import com.weebeeio.demo.domain.stats.dao.StatsDao;
import com.weebeeio.demo.domain.stats.repository.StatsRepository;
import com.weebeeio.demo.domain.login.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;


import java.util.Optional;

@Service
public class StatsService {

    @Autowired
    private StatsRepository statsRepository;

    @Autowired
    private UserService userService;

    StatsService(StatsRepository statsRepository){
        this.statsRepository = statsRepository;
    }


    public Optional<StatsDao> getStatsById(Integer userid) {
        return statsRepository.findByUser_UserId(userid);
    }
    


    public void save(StatsDao statsDao) {
        // StatsDao 저장
        statsRepository.save(statsDao);
        
        // 연관된 User가 있는 경우 함께 저장
        if (statsDao.getUser() != null) {
            userService.save(statsDao.getUser());
        }
    }
    
}
