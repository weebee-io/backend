// SurveyRequest.java
package com.weebeeio.demo.domain.survey.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

@Getter @Setter
public class SurveyRequest {
    @NotNull
    @JsonProperty("risk_profile_score")
    private Integer riskProfileScore;

    @NotNull
    @JsonProperty("complex_product_flag")
    private Integer complexProductFlag;

    @NotNull
    @JsonProperty("is_married")
    private Integer isMarried;

    @NotNull
    @JsonProperty("essential_pct")
    private Integer essentialPct;

    @NotNull
    @JsonProperty("discretionary_pct")
    private Integer discretionaryPct;

    @NotNull
    @JsonProperty("sav_inv_ratio")
    private Integer savInvRatio;

    @NotNull
    @JsonProperty("spend_volatility")
    private Integer spendVolatility;

    @NotNull
    @JsonProperty("digital_engagement")
    private Integer digitalEngagement;
}
