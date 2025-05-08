package com.weebeeio.demo.domain.survey.repository;

import com.weebeeio.demo.domain.survey.entity.Survey;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * Survey 엔티티에 대한 CRUD 및 조회 기능 제공
 */
public interface SurveyRepository extends JpaRepository<Survey, Integer> {

    /**
     * 해당 사용자가 이미 설문을 제출했는지 확인한다.
     * @param userId users.user_id
     * @return true면 존재
     */
    boolean existsByUserId(Integer userId);
}
