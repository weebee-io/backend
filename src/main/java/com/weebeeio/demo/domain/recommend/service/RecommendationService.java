package com.weebeeio.demo.domain.recommend.service;

import com.weebeeio.demo.domain.recommend.dao.ProductStock;
import com.weebeeio.demo.domain.recommend.dao.ProductDeposit;
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
    public RecommendationResponse getRecommendations(int userId) {
        List<ProductStock> stockResults = recommendationRepository.findStockRecommendations(userId);
        List<ProductDeposit> depositResults = recommendationRepository.findDepositRecommendations(userId);

        List<RecommendationResponse.StockRecommendation> stockRecommendations = stockResults.stream()
            .map(stock -> RecommendationResponse.StockRecommendation.builder()
                .stockNo(stock.getStockNo())
                .stockName(stock.getStockName())
                .stockRank(stock.getStockRank())
                .stockDetail(stock.getStockDetail())
                .stockUrl(stock.getStockUrl())
                .build())
            .collect(Collectors.toList());

        List<RecommendationResponse.DepositRecommendation> depositRecommendations = depositResults.stream()
            .map(deposit -> RecommendationResponse.DepositRecommendation.builder()
                .depositNo(deposit.getDepositNo())
                .depositName(deposit.getDepositName())
                .depositRank(deposit.getDepositRank())
                .depositDetail(deposit.getDepositDetail())
                .build())
            .collect(Collectors.toList());

        return RecommendationResponse.builder()
            .userId(userId)
            .stockRecommendations(stockRecommendations)
            .depositRecommendations(depositRecommendations)
            .build();
    }
} 