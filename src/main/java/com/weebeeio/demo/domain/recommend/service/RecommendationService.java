package com.weebeeio.demo.domain.recommend.service;

import com.weebeeio.demo.domain.recommend.dao.FinanceProduct;
import com.weebeeio.demo.domain.login.entity.User;
import com.weebeeio.demo.domain.recommend.dto.RecommendationResponse;
import com.weebeeio.demo.domain.recommend.repository.RecommendationRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class RecommendationService {
    
    private final RecommendationRepository recommendationRepository;

    @Transactional(readOnly = true)
    public RecommendationResponse getRecommendations(User user) {
        List<FinanceProduct> products = recommendationRepository.findProductsByUserRank(user.getUserSegment());

        List<RecommendationResponse.FinanceProductDto> recommendedProducts = products.stream()
            .map(product -> RecommendationResponse.FinanceProductDto.builder()
                .productId(product.getProductId())
                .productName(product.getProductName())
                .productDetail(product.getProductDetail())
                .productRank(product.getProductRank())
                .productUrl(product.getProductUrl())
                .build())
            .collect(Collectors.toList());

        return RecommendationResponse.builder()
            .userId(user.getUserId())
            .userRank(user.getUserSegment())
            .recommendedProducts(recommendedProducts)
            .build();
    }
} 