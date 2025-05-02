package com.weebeeio.demo.domain.leaderboard.controller;


import com.weebeeio.demo.domain.leaderboard.dto.LeaderboardDto;
import com.weebeeio.demo.domain.leaderboard.service.LeaderboardService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.web.bind.annotation.*;

@Tag(name = "LeaderBoard", description = "리더보드")
@RestController
@RequestMapping("/leaderboard")
public class LeaderboardController {

    @Autowired
    private LeaderboardService leaderboardService;

    @GetMapping
    @Operation(summary = "회원 스탯 페이징 조회", description = "statSum 내림차순으로 페이지 단위 조회")
    public Page<LeaderboardDto> getStatsPage(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size
    ) {
        return leaderboardService.getUserStatsPage(page, size);
    }

}
