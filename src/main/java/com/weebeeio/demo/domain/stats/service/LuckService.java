package com.weebeeio.demo.domain.stats.service;

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


    @Scheduled(cron = "0 0 0 * * *")
    @Transactional
    public void resetDailyLuck() {
        logger.info("luck 스탯 갱신 시작");
        
        List<StatsDao> allStats = statsRepository.findAll();
        
        for (StatsDao stats : allStats) {
            int newLuckValue = random.nextInt(101); // 0~100 사이 랜덤값
            stats.setLuckStat(newLuckValue);
            stats.getUser().setCoin(stats.getUser().getCoin() + newLuckValue);
            statsRepository.save(stats);
            logger.debug("사용자 ID: {}, 새로운 luck 스탯: {}", 
                stats.getUser().getUserId(), newLuckValue);
        }
        
        logger.info("총 {} 명의 사용자 luck 스탯 갱신 완료", allStats.size());
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
