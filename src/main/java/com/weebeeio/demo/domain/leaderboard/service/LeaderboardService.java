package com.weebeeio.demo.domain.leaderboard.service;

import com.weebeeio.demo.domain.leaderboard.dto.LeaderboardDto;
import com.weebeeio.demo.domain.leaderboard.repository.LeaderboardRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class LeaderboardService {


    private final LeaderboardRepository leaderboardRepository;

    /**
     * statSum 내림차순으로 전체 Stats 조회 + 페이징
     */
    public Page<LeaderboardDto> getUserStatsPage(int page, int size) {
        PageRequest pageRequest = PageRequest.of(
                page,
                size,
                Sort.by("statSum").descending()
        );

        return leaderboardRepository
                .findAll(pageRequest)
                .map(LeaderboardDto::from);
    }

}
