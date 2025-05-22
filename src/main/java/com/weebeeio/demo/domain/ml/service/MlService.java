package com.weebeeio.demo.domain.ml.service;

import java.util.Optional;

import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.springframework.kafka.annotation.KafkaListener;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import com.weebeeio.demo.domain.login.entity.User;
import com.weebeeio.demo.domain.login.service.UserService;
import com.weebeeio.demo.domain.stats.dao.StatsDao;
import com.weebeeio.demo.domain.stats.service.StatsService;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

@Service    
@RequiredArgsConstructor
public class MlService {
    
    private static final String TOPIC = "clustering_results";

    private final UserService userService;
    private final StatsService statsService;

    private static final Logger logger = LoggerFactory.getLogger(MlService.class);
    
    // 클러스터링 결과 메시지를 위한 DTO
    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    @JsonIgnoreProperties(ignoreUnknown = true)
    public static class ClusteringResult {
        @JsonProperty("lit_level")
        private String litLevel;
        
        @JsonProperty("proba")
        private Double proba;
    }

    @KafkaListener(
        topics = TOPIC,
        groupId = "my-consumer-group",
        properties = {
            "spring.json.trusted.packages=*",
            "spring.json.value.default.type=com.weebeeio.demo.domain.ml.service.MlService$ClusteringResult"
        }
    )
    
    public void getClusterResult(ConsumerRecord<String, ClusteringResult> record) {
        try {
            String key = record.key();
            ClusteringResult clusteringResult = record.value();
            
            if (key == null || clusteringResult == null) {
                logger.warn("카프카에서 null 키 또는 값을 받았습니다");
                return;
            }
            
            logger.debug("카프카 메시지 수신 - 키={}, 값={}", key, clusteringResult);
            
            // 키에서 사용자 ID 파싱
            int userId;
            try {
                userId = Integer.parseInt(key);
            } catch (NumberFormatException e) {
                logger.error("유효하지 않은 사용자 ID 형식: {}", key, e);
                return;
            }
            
            // 데이터베이스에서 사용자 정보 가져오기
            Optional<User> userOptional = userService.getUserInfo(userId);
            if (!userOptional.isPresent()) {
                logger.warn("ID: {}에 해당하는 사용자를 찾을 수 없습니다", userId);
                return;
            }
            
            User user = userOptional.get();
            
            // ClusteringResult에서 litLevel 가져오기
            String litLevel = clusteringResult.getLitLevel();
            if (litLevel != null) {
                logger.info("사용자 {}의 등급을 {}로 설정합니다", userId, litLevel);
                
                // litLevel 기반으로 사용자 등급 설정
                user.setUserrank(litLevel);
                // 업데이트에 따라 유저 랭크 계산

                StatsDao stats = new StatsDao();
                stats.setUser(user);

                switch (litLevel) {
                    case "GOLD":
                        stats.setInvestStat(400);
                        stats.setCreditStat(400);
                        stats.setFiStat(400);
                        break;
                    case "SILVER":
                        stats.setInvestStat(300);
                        stats.setCreditStat(300);
                        stats.setFiStat(300);
                        break;
                    case "BRONZE":
                        stats.setInvestStat(100);
                        stats.setCreditStat(100);
                        stats.setFiStat(100);
                        break;
                }
                
                statsService.save(stats);
                // 업데이트된 사용자 저장
                userService.save(user);
                
                // 추가 정보 로깅
                logger.debug("클러스터링 확률값: {}", clusteringResult.getProba());
            } else {
                logger.warn("카프카 메시지에 litLevel이 null입니다");
            }
        } catch (Exception e) {
            logger.error("클러스터링 결과 처리 중 오류 발생", e);
        }
    }
}
