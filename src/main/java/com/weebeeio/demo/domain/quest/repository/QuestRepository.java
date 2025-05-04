package com.weebeeio.demo.domain.quest.repository;

import com.weebeeio.demo.domain.quest.entity.Quest;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface QuestRepository extends JpaRepository<Quest, Integer> {

    /** 아직 완료되지 않은 ‘ATTEND’ 퀘스트 한 건 조회 */
    Optional<Quest> findByUserIdAndQuestTypeAndQuestDoneFalse(
        Integer userId,
        String questType
    );
}
