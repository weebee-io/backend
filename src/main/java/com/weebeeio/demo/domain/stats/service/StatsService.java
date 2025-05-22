package com.weebeeio.demo.domain.stats.service;

import com.weebeeio.demo.domain.login.entity.User;
import com.weebeeio.demo.domain.stats.dao.StatsDao;
import com.weebeeio.demo.domain.stats.repository.StatsRepository;
import com.weebeeio.demo.domain.login.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
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

    /**
     * 사용자 ID로 가장 최근 통계 레코드를 찾습니다.
     * ID가 가장 큰 레코드를 반환합니다.
     */
    public Optional<StatsDao> getStatsById(Integer userid) {
        return statsRepository.findTopByUser_UserIdOrderByStatsIdDesc(userid);
    }
    
    /**
     * 사용자 ID로 찾은 처음 통계 레코드를 반환합니다.
     * 데이터가 없는 경우 null을 반환합니다.
     */
    public StatsDao getFirstStatsByUserId(Integer userid) {
        Optional<StatsDao> statsList = getStatsById(userid);
        return statsList.orElse(null);
    }

    /**
     * StatsDao를 저장하고, 연관된 User도 함께 저장합니다.
     * StatsDao의 @PrePersist 및 @PreUpdate에서 User의 랭크가 계산되지만,
     * 해당 변경사항이 저장되려면 User도 함께 저장되어야 합니다.
     */
    public void save(StatsDao statsDao) {
        // StatsDao 저장
        statsRepository.save(statsDao);
        
        // 연관된 User가 있는 경우 함께 저장
        if (statsDao.getUser() != null) {
            userService.save(statsDao.getUser());
        }
    }
    
}
