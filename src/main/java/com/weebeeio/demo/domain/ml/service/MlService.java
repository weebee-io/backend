package com.weebeeio.demo.domain.ml.service;

import java.util.Collections;
import java.util.Map;
import java.util.Optional;

import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.springframework.kafka.annotation.KafkaListener;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import com.weebeeio.demo.domain.login.entity.User;
import com.weebeeio.demo.domain.login.service.UserService;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service    
@RequiredArgsConstructor
public class MlService {
    
    private static final String TOPIC = "clustering_userRank";

    private final UserService userService;

    private static final Logger logger = LoggerFactory.getLogger(MlService.class);

    @KafkaListener(
        topics = TOPIC,
        groupId = "my-consumer-group"
    )


    
    public void getClusterResult(ConsumerRecord<String, Map<String, Object>> record) {

        String key       = record.key();
        Map<String, Object> value     = record.value();

        Optional<User> user = userService.getUserInfo(Integer.parseInt(key));
        logger.debug("key={}, cluster={}",key, value);

        user.get().setUserrank(value.get("cluster_result").toString());

        userService.save(user.get());

        user.get().setQuizResults(Collections.emptyList());

    }
}
