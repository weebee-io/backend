package com.weebeeio.demo.domain.stats.controller;

import com.weebeeio.demo.domain.login.entity.User;
import com.weebeeio.demo.domain.stats.dao.StatsDao;
import com.weebeeio.demo.domain.stats.dto.UserStatsResponseDto;
import com.weebeeio.demo.domain.stats.service.StatsService;
import com.weebeeio.demo.domain.stats.service.LuckService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import java.util.NoSuchElementException;
import java.util.Optional;
import java.util.List;
import java.util.Map;
import java.util.HashMap;
import java.util.Random;

@Tag(name = "Stats", description = "사용자 스탯")
@RestController
@RequestMapping("/stats")
@RequiredArgsConstructor
public class StatsController {

    @Autowired
    private final StatsService statsService;

    @Autowired
    private final LuckService luckService;


    @GetMapping("/getuserstats")
    @Operation(summary = "회원 스탯 조회", description = "회원 스탯을 조회합니다.")
    public ResponseEntity<UserStatsResponseDto> getUserStats(
            @AuthenticationPrincipal User user) {
    
        Integer userId = user.getUserId();
                
        // 통계 데이터 가져오기
        StatsDao stats = statsService.getStatsById(userId)
                .orElseThrow(() -> new NoSuchElementException("Stats를 찾을 수 없습니다. ID=" + userId));

        // 계산된 최신 랭크 가져오기
        String userRank = user.getUserrank();

        // 최신 랭크로 UserStatsResponseDto 생성
        UserStatsResponseDto dto = new UserStatsResponseDto(stats, userRank);
    
        // 200 OK로 응답
        return ResponseEntity.ok(dto);
    }

    @GetMapping("/weebee-image")
    @Operation(summary = "회원의 Weebee 이미지 조회", description = "회원의 금융스탯 총합에 따른 Weebee 이미지 이름을 조회합니다.")
    public ResponseEntity<Map<String, String>> getWeebeeImage() {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        User user = (User) auth.getPrincipal();
        
        // 첫 번째 통계 레코드 가져오기
        Optional<StatsDao> stats = statsService.getStatsById(user.getUserId());
        if (stats.isEmpty()) {
            throw new NoSuchElementException("사용자 스탯이 없습니다. ID: " + user.getUserId());
        }
        return ResponseEntity.ok(Map.of("imageName", stats.get().getWeebeeImageName()));
    }


    

    @GetMapping("/luck")
    @Operation(summary = "운 스탯 조회", description = "현재 회원의 운 스탯을 조회합니다. 0~100 사이의 값으로 매일 자정에 리셋됩니다.")
    public ResponseEntity<Map<String, Object>> getLuck() {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        User user = (User) auth.getPrincipal();
        
        // 통계 데이터 가져오기
        StatsDao stats = statsService.getStatsById(user.getUserId())
                .orElseThrow(() -> new NoSuchElementException("사용자 스탯이 없습니다. ID: " + user.getUserId()));
        
        Integer luckStat = stats.getLuckStat();
        if (luckStat == null) {
            luckStat = 0;
            stats.setLuckStat(0);
            statsService.save(stats);
        }
        
        // 응답 데이터 구성
        Map<String, Object> response = new HashMap<>();
        response.put("luck", luckStat);
        response.put("message", getLuckMessage(luckStat));
        
        // 예시 보너스 점수 계산
        int baseScore = 100; // 기본 점수 예시
        int bonusScore = luckService.applyLuckBonus(baseScore, luckStat);
        response.put("exampleScore", baseScore);
        response.put("exampleBonusScore", bonusScore);
        response.put("bonusAmount", bonusScore - baseScore);
        
        return ResponseEntity.ok(response);
    }
    
    /**
     * 운 스탯 값에 따른 메시지를 반환합니다.
     */
    private String getLuckMessage(int luckValue) {
        if (luckValue >= 90) {
            return "대박! 오늘은 매우 행운의 날입니다! 문제를 풀면 최대 " + luckValue + "% 추가 점수를 얻을 수 있습니다.";
        } else if (luckValue >= 70) {
            return "운이 좋은 날입니다! 문제를 풀면 최대 " + luckValue + "% 추가 점수를 얻을 수 있습니다.";
        } else if (luckValue >= 50) {
            return "오늘은 평범한 날입니다. 문제를 풀면 최대 " + luckValue + "% 추가 점수를 얻을 수 있습니다.";
        } else if (luckValue >= 30) {
            return "오늘은 조금 운이 안 좋네요. 문제를 풀면 최대 " + luckValue + "% 추가 점수를 얻을 수 있습니다.";
        } else {
            return "운이 안 좋은 날입니다. 하지만 걱정하지 마세요! 내일은 더 좋아질 거예요. 문제를 풀면 최대 " + luckValue + "% 추가 점수를 얻을 수 있습니다.";
        }
    }
    
    @Operation(summary = "회원 스탯 초기화", description = "회원 스탯을 초기화합니다.")
    @GetMapping("/statsInit")
    public ResponseEntity<List<StatsDao>> statsInit() {
        // 1) SecurityContext에서 현재 로그인한 User 꼽내기
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        User user = (User) auth.getPrincipal();
        
        StatsDao stats = new StatsDao();
        stats.setUser(user);
        stats.setInvestStat(0);
        stats.setCreditStat(0);
        stats.setFiStat(0);
        stats.setLuckStat(new Random().nextInt(101)); // 0~100 사이의 랜덤 운 스탯 설정
        // statSum은 @PrePersist에 의해 자동 계산됨

        // 저장
        statsService.save(stats);

        return ResponseEntity.ok().build();
    }
}
