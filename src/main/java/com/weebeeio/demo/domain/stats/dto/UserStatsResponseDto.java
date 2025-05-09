package com.weebeeio.demo.domain.stats.dto;

import com.weebeeio.demo.domain.stats.dao.StatsDao;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class UserStatsResponseDto {
    private StatsDao stats;
    private String userRank;
}