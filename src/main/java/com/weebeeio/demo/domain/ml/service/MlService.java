package com.weebeeio.demo.domain.ml.service;

import java.util.Collections;

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


    
    public void getClusterResult(ConsumerRecord<String, String> record, User user) {

        String key       = record.key();
        String value     = record.value();

        logger.debug("key={}, cluster={}",key, value);

        switch (value) {
            case "0": user.setUserrank("브론즈"); break;
            case "1": user.setUserrank("실버");   break;
            case "2": user.setUserrank("골드");   break;
            default: user.setUserrank("언랭"); break;
        }

        userService.save(user);

        user.setQuizResults(Collections.emptyList());

    }
}
