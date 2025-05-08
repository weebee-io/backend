package com.weebeeio.demo.domain.survey.entity;

import com.weebeeio.demo.domain.login.entity.User;
import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "survey")
@Getter @Setter
@NoArgsConstructor @AllArgsConstructor
@Builder
public class Survey {

    /** PK로 사용할 user_id (users.user_id) */
    @Id
    @Column(name = "user_id")
    private Integer userId;

    /** User 엔티티와 1:1 매핑 (PK 공유) */
    @OneToOne(fetch = FetchType.LAZY)
    @MapsId
    @JoinColumn(name = "user_id")
    private User user;

    /** 투자 상품 유형 */
    @Column(name = "asset_type", nullable = false)
    private Integer assetType;

    /** 투자 공부 수단 */
    @Column(name = "invest_resource", nullable = false)
    private Integer investResource;

    /** 신용 점수 */
    @Column(name = "credit_score", nullable = false)
    private Integer creditScore;

    /** 연체 여부 (횟수) */
    @Column(name = "delinquent_count", nullable = false)
    private Integer delinquentCount;

    /** 소득 대비 부채 비율 */
    @Column(name = "debt_ratio", nullable = false)
    private Integer debtRatio;

    /** 소비 습관 점수 */
    @Column(name = "consumption_score", nullable = false)
    private Integer consumptionScore;

    /** 디지털 친화도 */
    @Column(name = "digital_friendly", nullable = false)
    private Integer digitalFriendly;

    /** 금융 지식 퀴즈 점수 */
    @Column(name = "fin_know_score", nullable = false)
    private Integer finKnowScore;

}
