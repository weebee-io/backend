package com.weebeeio.demo.domain.survey.controller;

import com.weebeeio.demo.domain.survey.dto.SurveyRequest;
import com.weebeeio.demo.domain.survey.dto.SurveyResponse;
import com.weebeeio.demo.domain.survey.service.SurveyService;
import com.weebeeio.demo.domain.login.entity.User;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

/**
 * 설문조사 관련 REST API를 제공하는 컨트롤러
 */
@Tag(name = "설문조사")
@RestController
@RequestMapping("/surveys")
@RequiredArgsConstructor
public class SurveyController {

    private final SurveyService surveyService;

    /**
     * 설문 응답을 일괄 저장하는 엔드포인트
     *
     * @param req        검증된 설문 요청 DTO
     * @param principal  인증된 사용자 정보 (Spring Security)
     * @return           저장된 설문 정보 및 세그먼트 DTO
     */
    @Operation(summary = "설문 제출")
    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public SurveyResponse submit(
        @Validated @RequestBody SurveyRequest req,
        @AuthenticationPrincipal User principal) {
        
        // 서비스 호출 후 DTO 반환
        return surveyService.submitSurvey(principal.getUserId(), req);
    }
}