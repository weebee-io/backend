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


    @Column(name = "risk_profile_score", nullable = false)
    private Integer riskProfileScore;


    @Column(name = "complex_product_flag", nullable = false)
    private Integer complexProductFlag;

    @Column(name = "is_married", nullable = false)
    private Integer isMarried;

    @Column(name = "essential_pct", nullable = false)
    private Integer essentialPct;

    @Column(name = "discretionary_pct", nullable = false)
    private Integer discretionaryPct;

    @Column(name = "sav_inv_ratio", nullable = false)
    private Integer savInvRatio;


    @Column(name = "spend_volatility", nullable = false)
    private Integer spendVolatility;


    @Column(name = "digital_engagement", nullable = false)
    private Integer digitalEngagement;



}
