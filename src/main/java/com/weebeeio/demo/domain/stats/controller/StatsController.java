package com.weebeeio.demo.domain.stats.controller;

import com.weebeeio.demo.domain.login.entity.User;
import com.weebeeio.demo.domain.stats.dao.StatsDao;
import com.weebeeio.demo.domain.stats.service.StatsService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Optional;
import java.util.List;


@Tag(name = "Stats", description = "사용자 스탯")
@RestController
@RequestMapping("/stats")
@RequiredArgsConstructor
public class StatsController {

    @Autowired
    private final StatsService statsService;



    @GetMapping("/{userId}")
    @Operation(summary = "회원 스탯 조회", description = "회원 스탯을 조회합니다.")
    public Optional<StatsDao> getUserStats(){
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        User user = (User) auth.getPrincipal();
        

        return statsService.getStatsById(user.getUserId());

    }


    @Operation(summary = "회원 스탯 초기화", description = "회원 스탯을 초기화합니다.")
    @GetMapping("/statsInit")
    public ResponseEntity<List<StatsDao>> statsInitt() {
        // 1) SecurityContext에서 현재 로그인한 User 꺼내기
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        User user = (User) auth.getPrincipal();
        

        StatsDao stats = new StatsDao();
        stats.setUser(user);
        stats.setInvestStat(0);
        stats.setCreditStat(0);
        stats.setFiStat(0);
        // statSum은 @PrePersist에 의해 자동 계산됨

        // 저장
        statsService.save(stats);

        return ResponseEntity.ok().build();

    }
    



}
