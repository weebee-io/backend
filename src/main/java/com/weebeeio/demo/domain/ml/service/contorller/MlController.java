package com.weebeeio.demo.domain.ml.service.contorller;

import java.util.Collections;
import java.util.List;


import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


import com.weebeeio.demo.domain.login.entity.User;
import com.weebeeio.demo.domain.login.service.UserService;
import com.weebeeio.demo.domain.ml.dto.ClusterResponseDto;
import com.weebeeio.demo.domain.survey.entity.Survey;
import com.weebeeio.demo.domain.survey.service.SurveyService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;

@Tag(name = "ML API", description = "설문결과로 클러스터링 요청하는는 API")
@RestController
@RequestMapping("/Ml")
@RequiredArgsConstructor
public class MlController {

    private final SurveyService surveyService;
    private final RestTemplate restTemplate;
    private final UserService userService;
    private static final Logger logger = LoggerFactory.getLogger(MlController.class);





    @Operation(summary = "클러스터링 하기", description = "클러스터링 후 유저의 랭크를 기록합니다.")
    @GetMapping("/clustering")
    public ResponseEntity<List<User>> getquiz(@AuthenticationPrincipal User user) {

        Survey survey = surveyService.getsurveybyUserid(user.getUserId())
                .orElseThrow(() -> new IllegalArgumentException("해당 사용자의 설문이 없습니다. ID: " + user.getUserId()));




                
        String fastApiUrl = "http://localhost:8000/cluster";





        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        // Survey 객체를 바로 직렬화할 수도 있지만, 필요시 DTO 로 변환해서 보내세요.
        HttpEntity<Survey> requestEntity = new HttpEntity<>(survey, headers);

        ResponseEntity<ClusterResponseDto> response = restTemplate
                .postForEntity(fastApiUrl, requestEntity, ClusterResponseDto.class);


        if (response == null || !response.hasBody()) {
            logger.error("FastAPI 응답이 없습니다.");
            // 예외 던지거나 기본값 할당
            throw new IllegalStateException("FastAPI에서 응답이 없습니다.");
        }
        
        String rank = response.getBody().getRank();
        


        switch (rank) {
            case "0":
                user.setUserrank("브론즈");
                break;
            case "1":
                user.setUserrank( "실버");
                break;
            case "2":
                user.setUserrank("골드");
                break;
        }
       
        userService.save(user);

        return ResponseEntity.ok(Collections.singletonList(user));
    }





    
}
