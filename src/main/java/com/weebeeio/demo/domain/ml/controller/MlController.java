package com.weebeeio.demo.domain.ml.controller;

import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;


import com.weebeeio.demo.domain.login.entity.User;
import com.weebeeio.demo.domain.survey.entity.Survey;
import com.weebeeio.demo.domain.survey.service.SurveyService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;

@Tag(name = "ML API", description = "설문결과로 클러스터링 요청하는 API")
@RestController
@RequestMapping("/ml")
@RequiredArgsConstructor
public class MlController {

    private final SurveyService surveyService;

   
    @Autowired
    private KafkaTemplate<String, Map<String, Object>> kafkaTemplate;

    private static final String TOPIC = "clustering_userRank";



    @Operation(summary = "클러스터링 카프카로 전송", description = "카프카를 통해 클러스터링 후 유저의 랭크를 기록.")
    @GetMapping("/clusteringwithKafka")
    public ResponseEntity<String> clusteringwithKafka(@AuthenticationPrincipal User user) {
        // 1) 설문 조회
        Survey survey = surveyService.getsurveybyUserid(user.getUserId())
            .orElseThrow(() -> new IllegalArgumentException("해당 사용자의 설문이 없습니다. ID: " + user.getUserId()));

        // 2) FastAPI 요청 바디 생성
        Map<String, Object> payload = new HashMap<>();
        payload.put("risk_profile_score",               user.getAge());
        payload.put("complex_product_flag",            1);
        payload.put("is_married",       survey.getRiskProfileScore());
        payload.put("essential_pct",     survey.getEssentialPct());
        payload.put("discretionary_pct",      survey.getDiscretionaryPct());
        payload.put("sav_inv_ratio",      survey.getSavInvRatio());
        payload.put("spend_volatility",  survey.getSpendVolatility());
        payload.put("digital_engagement",        survey.getDigitalEngagement());

        kafkaTemplate.send(TOPIC, payload);

        return ResponseEntity.ok("클러스터링 요청 완료");
    }


}