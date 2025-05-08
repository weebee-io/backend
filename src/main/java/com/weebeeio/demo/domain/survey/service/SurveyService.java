package com.weebeeio.demo.domain.survey.service;

import com.weebeeio.demo.domain.survey.dto.SurveyRequest;
import com.weebeeio.demo.domain.survey.dto.SurveyResponse;
import com.weebeeio.demo.domain.survey.entity.Survey;
import com.weebeeio.demo.domain.survey.repository.SurveyRepository;
import com.weebeeio.demo.domain.login.entity.User;
import com.weebeeio.demo.domain.login.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * 설문 저장 및 조회 로직을 담당하는 서비스 클래스
 */
@Service
@RequiredArgsConstructor
public class SurveyService {

    private final SurveyRepository surveyRepository;
    private final UserRepository userRepository;

    /**
     * 설문을 처음 제출할 때 호출하는 메서드
     *
     * @param userId  요청을 보낸 사용자 PK
     * @param req     클라이언트에서 전송한 설문 응답 DTO
     * @return        저장된 설문 정보와 (현재는 null인) 세그먼트 결과 DTO
     * @throws IllegalStateException    이미 제출된 설문인 경우
     * @throws IllegalArgumentException 사용자 정보가 유효하지 않은 경우
     */
    @Transactional
    public SurveyResponse submitSurvey(Integer userId, SurveyRequest req) {
        // 1) 중복 제출 방지: 이미 존재하면 예외 발생
        if (surveyRepository.existsByUserId(userId)) {
            throw new IllegalStateException("이미 설문을 제출했습니다.");
        }

        // 2) 사용자 엔티티 조회
        User user = userRepository.findById(userId)
            .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 사용자입니다."));

        // 3) Survey 엔티티 생성 및 필드 매핑
        Survey survey = Survey.builder()
            .user(user)
            .assetType(req.getAssetType())
            .investResource(req.getInvestResource())
            .creditScore(req.getCreditScore())
            .delinquentCount(req.getDelinquentCount())
            .debtRatio(req.getDebtRatio())
            .consumptionScore(req.getConsumptionScore())
            .digitalFriendly(req.getDigitalFriendly())
            .finKnowScore(req.getFinKnowScore())
            .build();

        // 4) DB에 설문 응답 저장
        survey = surveyRepository.save(survey);

        // 5) TODO: FastAPI 예측 모델 호출 후 user.setUserSegment(...) 및 userRepository.save(user)

        // 6) 저장 결과를 DTO로 변환하여 반환
        return new SurveyResponse(
            survey.getUserId(),
            survey.getAssetType(),
            survey.getInvestResource(),
            survey.getCreditScore(),
            survey.getDelinquentCount(),
            survey.getDebtRatio(),
            survey.getConsumptionScore(),
            survey.getDigitalFriendly(),
            survey.getFinKnowScore(),
            /* 예측 결과 */ null
        );
    }
}