package com.weebeeio.demo.domain.kafkaTest.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;

@RestController
@RequestMapping("/kafka")
@Tag(name = "카프카 테스트 API", description = "카프카 테스트를 진행하는 API")
public class ProducerController {
    
    @Autowired
    private KafkaTemplate<String, String> kafkaTemplate;

    private static final String TOPIC = "jeongsooidiot";

    @Operation(summary = "카프카 테스트", description = "메시지를 Kafka 토픽에 전송합니다.")
    @GetMapping("/send")
    public String sendMessage() {
        kafkaTemplate.send(TOPIC, "정수야 프론트로 돌아와");
        return "메시지 전송 완료!";
    }
    
}
