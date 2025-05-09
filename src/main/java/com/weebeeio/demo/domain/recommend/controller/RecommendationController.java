package com.weebeeio.demo.domain.recommend.controller;

import com.weebeeio.demo.domain.recommend.dto.RecommendationResponse;
import com.weebeeio.demo.domain.recommend.service.RecommendationService;
import com.weebeeio.demo.domain.login.entity.User;
import com.weebeeio.demo.domain.login.repository.UserRepository;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Tag(name = "금융 상품 추천 API", description = "사용자 금융 이해도 기반 맞춤형 금융 상품 추천 API")
@Controller
@RequestMapping("/recommendations")
@RequiredArgsConstructor
public class RecommendationController {

    private final RecommendationService recommendationService;
    private final UserRepository userRepository;

    @Operation(
        summary = "금융 상품 추천 조회 API",
        description = "사용자의 금융 이해도(주식, 예/적금)에 따른 맞춤형 금융 상품을 추천합니다."
    )
    @GetMapping("/api/getRecommendations")
    @ResponseBody
    public ResponseEntity<RecommendationResponse> getRecommendations(
        @Parameter(description = "사용자 ID", required = true)
        @AuthenticationPrincipal User user
    ) {
        User users = userRepository.findById(user.getUserId())
            .orElseThrow(() -> new RuntimeException("User not found with id: " + user.getUserId()));
        RecommendationResponse response = recommendationService.getRecommendations(users);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/showRecommendations")
    public String showRecommendations(@AuthenticationPrincipal User user, Model model) {
        User users = userRepository.findById(user.getUserId())
                .orElseThrow(() -> new RuntimeException("User not found with id: " + user.getUserId()));

        RecommendationResponse recommendations = recommendationService.getRecommendations(users);

        model.addAttribute("recommendations", recommendations); // 추천 리스트
        model.addAttribute("user", user);  // 사용자 정보

        return "recommendations/result";
    }

} 