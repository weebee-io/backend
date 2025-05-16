package com.weebeeio.demo.domain.kafkaTest.service;

import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;


@Service    
public class ConsumerService {

    private static final String TOPIC = "jeongsooidiot";

    @KafkaListener(
        topics = TOPIC,
        groupId = "my-consumer-group"
    )
    
    public void listen(ConsumerRecord<String, String> record) {
        // 받은 메시지 정보
        String key       = record.key();
        String value     = record.value();
        int partition    = record.partition();
        long offset      = record.offset();

        System.out.printf(
            "▶ Consumed message -> topic=%s, partition=%d, offset=%d, key=%s, value=%s%n",
            record.topic(), partition, offset, key, value
        );

        // TODO: 비즈니스 로직 처리 (예: DB 저장 등)
        // (커밋은 자동으로 수행됩니다)
    }
}