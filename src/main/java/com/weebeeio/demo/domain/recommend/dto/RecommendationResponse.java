package com.weebeeio.demo.domain.recommend.dto;

import lombok.Builder;
import lombok.Getter;
import java.util.List;

@Getter
@Builder
public class RecommendationResponse {
    private int userId;
    private List<StockRecommendation> stockRecommendations;
    private List<DepositRecommendation> depositRecommendations;

    @Getter
    @Builder
    public static class StockRecommendation {
        private int stockNo;
        private String stockName;
        private int stockRank;
        private String stockDetail;
        private String stockUrl;
    }

    @Getter
    @Builder
    public static class DepositRecommendation {
        private int depositNo;
        private String depositName;
        private int depositRank;
        private String depositDetail;
    }
} 