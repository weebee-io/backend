package com.weebeeio.demo.domain.ml.service.contorller;

import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

import com.weebeeio.demo.domain.login.entity.User;
import com.weebeeio.demo.domain.login.service.UserService;
import com.weebeeio.demo.domain.ml.dto.ClusterResponseDto;
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
    private final RestTemplate restTemplate;
    private final UserService userService;
    private static final Logger logger = LoggerFactory.getLogger(MlController.class);

    @Operation(summary = "클러스터링 하기", description = "클러스터링 후 유저의 랭크를 기록합니다.")
    @GetMapping("/clustering")
    public ResponseEntity<List<User>> clustering(@AuthenticationPrincipal User user) {
        // 1) 설문 조회
        Survey survey = surveyService.getsurveybyUserid(user.getUserId())
            .orElseThrow(() -> new IllegalArgumentException("해당 사용자의 설문이 없습니다. ID: " + user.getUserId()));

        // 2) FastAPI 요청 바디 생성
        Map<String, Object> payload = new HashMap<>();
        payload.put("AGE",               user.getAge());
        payload.put("SEX_CD",            1);
        payload.put("INVEST_TYPE",       survey.getAssetType());
        payload.put("RESOURCE_USED",     survey.getInvestResource());
        payload.put("INVEST_RATIO",      0);
        payload.put("CREDIT_SCORE",      survey.getCreditScore());
        payload.put("DELINQUENT_COUNT",  survey.getDelinquentCount());
        payload.put("DEBT_RATIO",        survey.getDebtRatio());
        payload.put("Q1_SCORE",          0);
        payload.put("Q2_SCORE",          0);
        payload.put("Q3_SCORE",          0);
        payload.put("CONSUMPTION_SCORE", survey.getConsumptionScore());
        payload.put("FIN_KNOW_SCORE",    survey.getFinKnowScore());
        payload.put("DIGITAL_FRIENDLY",  survey.getDigitalFriendly());

        String fastApiUrl = "http://localhost:8000/predict/";

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        HttpEntity<Map<String,Object>> requestEntity = new HttpEntity<>(payload, headers);

        // 3) FastAPI 호출
        ResponseEntity<ClusterResponseDto> response = restTemplate
            .postForEntity(fastApiUrl, requestEntity, ClusterResponseDto.class);

        if (response == null || !response.hasBody()) {
            logger.error("FastAPI 응답이 없습니다.");
            throw new IllegalStateException("FastAPI에서 응답이 없습니다.");
        }

        ClusterResponseDto dto = response.getBody();
        int cluster = dto.getCluster();
        double proba = dto.getProba();
        logger.debug("cluster={}, proba={}", cluster, proba);

        // 4) cluster → 한글 랭크 매핑
        switch (cluster) {
            case 0: user.setUserrank("브론즈"); break;
            case 1: user.setUserrank("실버");   break;
            case 2: user.setUserrank("골드");   break;
            default: user.setUserrank("언랭랭");
        }

        // 5) DB 저장
        userService.save(user);

        user.setQuizResults(Collections.emptyList());
        // 6) 결과 반환
        return ResponseEntity.ok(Collections.singletonList(user));
    }
}