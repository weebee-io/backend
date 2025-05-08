package com.weebeeio.demo.domain.survey.dto;

import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

/**
 * 사용자가 설문 응답을 한 번에 제출할 때 사용하는 요청 DTO
 */
@Getter @Setter
public class SurveyRequest {
    
    /** 자산 유형 (asset_type 컬럼에 매핑) */
    @NotNull
    private Integer assetType;
    
    /** 투자 공부 수단 (invest_resource 컬럼에 매핑) */
    @NotNull
    private Integer investResource;
    
    /** 신용 점수 (credit_score 컬럼에 매핑) */
    @NotNull
    private Integer creditScore;
    
    /** 연체 횟수 (delinquent_count 컬럼에 매핑) */
    @NotNull
    private Integer delinquentCount;
    
    /** 부채 비율 (debt_ratio 컬럼에 매핑) */
    @NotNull
    private Integer debtRatio;
    
    /** 소비 습관 점수 (consumption_score 컬럼에 매핑) */
    @NotNull
    private Integer consumptionScore;
    
    /** 디지털 친화도 (digital_friendly 컬럼에 매핑) */
    @NotNull
    private Integer digitalFriendly;
    
    /** 금융 지식 퀴즈 점수 (fin_know_score 컬럼에 매핑) */
    @NotNull
    private Integer finKnowScore;
}