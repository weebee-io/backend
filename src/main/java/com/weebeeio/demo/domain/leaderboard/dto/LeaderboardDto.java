package com.weebeeio.demo.domain.leaderboard.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class LeaderboardDto {
    private Integer userId;
    private String nickname;
    private Integer statSum;

    public static LeaderboardDto from(com.weebeeio.demo.domain.stats.dao.StatsDao s) {
        return new LeaderboardDto(
                s.getUser().getUserId(),
                s.getUser().getNickname(),
                s.getStatSum()
        );
    }
}
