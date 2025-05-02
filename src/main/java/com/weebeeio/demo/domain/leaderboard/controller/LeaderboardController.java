package com.weebeeio.demo.domain.leaderboard.controller;


import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Tag(name = "Stats", description = "사용자 스탯")
@RestController
@RequestMapping("/stats")
public class LeaderboardController {
}
