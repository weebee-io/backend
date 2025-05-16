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
    private Integer risk_profile_score;
    
    /** 투자 공부 수단 */
    private Integer complex_product_flag;
    
    /** 신용 점수 */
    private Integer is_married;
    
    /** 연체 횟수 */
    private Integer essential_pct;
    
    /** 부채 비율 */
    private Integer discretionary_pct;
    
    /** 소비 습관 점수 */
    private Integer sav_inv_ratio;
    
    /** 디지털 친화도 */
    private Integer spend_volatility;
    
    /** 금융 지식 퀴즈 점수 */
    private Integer digital_engagement;
    
    /** 사용자 세그먼트 (예측 결과) */
    private String userSegment;  // FastAPI 호출 전에는 null
}