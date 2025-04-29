package com.weebeeio.demo.domain.recommend.controller;

import com.weebeeio.demo.domain.recommend.dto.RecommendationResponse;
import com.weebeeio.demo.domain.recommend.service.RecommendationService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@Tag(name = "금융 상품 추천 API", description = "사용자 금융 이해도 기반 맞춤형 금융 상품 추천 API")
@RestController
@RequestMapping("/api/v1/recommendations")
@RequiredArgsConstructor
public class RecommendationController {

    private final RecommendationService recommendationService;

    @Operation(
        summary = "금융 상품 추천 조회",
        description = "사용자의 금융 이해도(주식, 예/적금)에 따른 맞춤형 금융 상품을 추천합니다."
    )
    @GetMapping("/{userId}")
    public ResponseEntity<RecommendationResponse> getRecommendations(
        @Parameter(description = "사용자 ID", required = true)
        @PathVariable int userId
    ) {
        RecommendationResponse response = recommendationService.getRecommendations(userId);
        return ResponseEntity.ok(response);
    }
} 