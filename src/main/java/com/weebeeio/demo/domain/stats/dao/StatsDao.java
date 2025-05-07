package com.weebeeio.demo.domain.stats.dao;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.Data;
import com.weebeeio.demo.domain.login.entity.User;

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

    @Column(name = "stat_sum")
    private Integer statSum;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    @JsonIgnore
    private User user;

    @PrePersist
    @PreUpdate
    private void calculateSum() {
        int sum = (investStat != null ? investStat : 0)
                + (creditStat  != null ? creditStat  : 0)
                + (fiStat      != null ? fiStat      : 0);
        this.statSum = sum;
    }
}
