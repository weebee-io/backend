package com.weebeeio.demo.domain.stats.controller;


import com.weebeeio.demo.domain.login.dto.CommonResponseDto;
import com.weebeeio.demo.domain.login.entity.User;
import com.weebeeio.demo.domain.login.service.UserService;
import com.weebeeio.demo.domain.stats.dao.StatsDao;
import com.weebeeio.demo.domain.stats.service.StatsService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Optional;

@Tag(name = "Stats", description = "사용자 스탯")
@RestController
@RequestMapping("/stats")
@RequiredArgsConstructor
public class StatsController {

    private final StatsService statsService;

    @GetMapping("/{userId}")
    @Operation(summary = "회원 스탯 조회", description = "회원 스탯을 조회합니다.")
    public Optional<StatsDao> getUserStats(@PathVariable Integer userId){
        return statsService.getStatsById(userId);

    }




}
