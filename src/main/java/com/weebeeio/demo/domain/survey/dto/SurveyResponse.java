package com.weebeeio.demo.domain.survey.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * 저장된 설문 응답과 예측된 세그먼트를 반환할 때 사용하는 응답 DTO
 */
@Getter
@AllArgsConstructor
public class SurveyResponse {
    
    /** 사용자 PK (users.user_id) */
    private Integer userId;
    
    /** 자산 유형 */
    private Integer assetType;
    
    /** 투자 공부 수단 */
    private Integer investResource;
    
    /** 신용 점수 */
    private Integer creditScore;
    
    /** 연체 횟수 */
    private Integer delinquentCount;
    
    /** 부채 비율 */
    private Integer debtRatio;
    
    /** 소비 습관 점수 */
    private Integer consumptionScore;
    
    /** 디지털 친화도 */
    private Integer digitalFriendly;
    
    /** 금융 지식 퀴즈 점수 */
    private Integer finKnowScore;
    
    /** 사용자 세그먼트 (예측 결과) */
    private String userSegment;  // FastAPI 호출 전에는 null
}