package com.weebeeio.demo.domain.stats.service;

import com.weebeeio.demo.domain.login.entity.User;
import com.weebeeio.demo.domain.login.repository.UserRepository;
import com.weebeeio.demo.domain.stats.dao.StatsDao;
import com.weebeeio.demo.domain.stats.repository.StatsRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Random;

@Service
public class LuckService {

    private static final Logger logger = LoggerFactory.getLogger(LuckService.class);
    private final Random random = new Random();

    @Autowired
    private StatsRepository statsRepository;


    @Autowired
    private UserRepository userRepository;

    @Scheduled(cron = "0 0 0 * * *")
    @Transactional
    public void resetDailyLuck() {
        logger.info("luck 스탯 갱신 시작");
        
        // 사용자 기반으로 처리
        List<User> allUsers = userRepository.findAll();
        int updatedCount = 0;
        
        for (User user : allUsers) {
            try {
                // 사용자의 모든 스탯 조회
                List<StatsDao> userStats = statsRepository.findAllByUser_UserId(user.getUserId());
                
                StatsDao statsToUpdate;
                if (userStats.isEmpty()) {
                    // 스탯이 없는 경우 새로 생성
                    statsToUpdate = new StatsDao();
                    statsToUpdate.setUser(user);
                    statsToUpdate.setInvestStat(0);
                    statsToUpdate.setCreditStat(0);
                    statsToUpdate.setFiStat(0);
                    statsToUpdate.setNewsStat(0);
                    statsToUpdate.setLuckStat(0);
                } else {
                    // 가장 최근 스탯 사용 (ID가 가장 큰 것)
                    userStats.sort((a, b) -> b.getStatsId().compareTo(a.getStatsId()));
                    statsToUpdate = userStats.get(0);
                }
                
                // 새로운 luck 값 설정
                int newLuckValue = random.nextInt(101); // 0~100 사이 랜덤값
                int oldCoin = user.getCoin();
                int newCoin = oldCoin + newLuckValue;
                
                // 스탯 및 코인 업데이트
                statsToUpdate.setLuckStat(newLuckValue);
                user.setCoin(newCoin);
                
                // 저장
                userRepository.save(user);
                statsRepository.save(statsToUpdate);
                
                logger.info("사용자 ID: {}, 이전 코인: {}, 새 코인: {}, 행운 값: {}", 
                        user.getUserId(), oldCoin, newCoin, newLuckValue);
                
                updatedCount++;
            } catch (Exception e) {
                logger.error("사용자 ID: {}의 luck 업데이트 중 오류: {}", user.getUserId(), e.getMessage(), e);
            }
        }
        
        logger.info("총 {} 명 중 {} 명의 사용자 luck 스탯 갱신 완료", allUsers.size(), updatedCount);
    }
    


    public int applyLuckBonus(int baseScore, int luckStat) {
        if (luckStat < 0 || luckStat > 100) {
            throw new IllegalArgumentException("luck 스탯은 0에서 100 사이의 값이어야 합니다.");
        }
        
        // luck 스탯의 %만큼 보너스 점수 추가
        double bonusMultiplier = 1.0 + (luckStat / 100.0);
        return (int) Math.round(baseScore * bonusMultiplier);
    }
}
