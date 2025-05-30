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
        
        List<StatsDao> allStats = statsRepository.findAll();
        int updatedCount = 0;
        
        for (StatsDao stats : allStats) {
            try {
                // 사용자 엔티티 직접 조회 - 영속성 컨텍스트에서 최신 상태 확보
                User user = userRepository.findById(stats.getUser().getUserId())
                    .orElseThrow(() -> new RuntimeException("사용자를 찾을 수 없습니다: " + stats.getUser().getUserId()));
                
                int newLuckValue = random.nextInt(101); // 0~100 사이 랜덤값
                int oldCoin = user.getCoin();
                int newCoin = oldCoin + newLuckValue;
                
                // 행운 스탯 설정
                stats.setLuckStat(newLuckValue);
                
                // 코인 증가
                user.setCoin(newCoin);
                
                logger.info("사용자 ID: {}, 이전 코인: {}, 추가된 코인: {}, 새로운 코인: {}", 
                    user.getUserId(), oldCoin, newLuckValue, newCoin);
                
                // 각각 저장 - 사용자 엔티티 먼저 저장
                userRepository.save(user);
                statsRepository.save(stats);
                
                updatedCount++;
            } catch (Exception e) {
                logger.error("사용자 luck 스탯 및 코인 갱신 중 오류 발생: {}", e.getMessage(), e);
            }
        }
        
        logger.info("총 {} 명 중 {} 명의 사용자 luck 스탯 및 코인 갱신 완료", allStats.size(), updatedCount);
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
