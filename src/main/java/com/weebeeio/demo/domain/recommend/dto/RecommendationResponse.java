package com.weebeeio.demo.domain.recommend.dto;

import lombok.Builder;
import lombok.Getter;
import java.util.List;

@Getter
@Builder
public class RecommendationResponse {
    private int userId;
    private String userRank;
    private List<FinanceProductDto> recommendedProducts;

    @Getter
    @Builder
    public static class FinanceProductDto {
        private int productId;
        private String productName;
        private String productDetail;
        private String productRank;
        private String productUrl;
    }
} 