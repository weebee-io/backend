package com.weebeeio.demo.domain.stats.dao;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.Data;
import com.weebeeio.demo.domain.login.entity.User;
import java.util.Random;

@Data
@Entity
@Table(name = "stats")
public class StatsDao {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "stats_id")
    private Integer statsId;

    @Column(name = "invest_stat")
    private Integer investStat;

    @Column(name = "credit_stat")
    private Integer creditStat;

    @Column(name = "fi_stat")
    private Integer fiStat;

    @Column(name = "news_stat")
    private Integer newsStat;

    @Column(name = "luck_stat")
    private Integer luckStat;

    @Column(name = "stat_sum")
    private Integer statSum;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    @JsonIgnore
    private User user;

    @PrePersist
    @PreUpdate
    private void calculateSumAndLuck() {
        // 스탯섬 계산
        int sum = (investStat != null ? investStat : 0)
                + (creditStat  != null ? creditStat  : 0)
                + (fiStat      != null ? fiStat      : 0)
                + (newsStat    != null ? newsStat    : 0);
        this.statSum = sum;
        
        // 사용자 랭크 계산
        if (user != null) {
            if (sum >= 1200) {
                user.setUserrank("GOLD");
            } else if (sum >= 900) {
                user.setUserrank("SILVER");
            } else {
                user.setUserrank("BRONZE");
            }
        }
        
        // 운 스탯 계산도 같이 처리
        this.luckStat = new Random().nextInt(100);
    }

    public String getWeebeeImageName() {
        return WeebeeLevel.fromStatSum(statSum).getImageName();
    }
}
