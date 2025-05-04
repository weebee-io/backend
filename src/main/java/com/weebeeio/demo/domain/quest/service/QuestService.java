package com.weebeeio.demo.domain.quest.service;

import com.weebeeio.demo.domain.login.entity.User;
import com.weebeeio.demo.domain.login.repository.UserRepository;
import com.weebeeio.demo.domain.quest.entity.Quest;
import com.weebeeio.demo.domain.quest.repository.QuestRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;

@Service
@RequiredArgsConstructor
public class QuestService {

    private final UserRepository  userRepo;
    private final QuestRepository questRepo;
    private static final int      REWARD = 50;
    private static final String   TYPE   = "ATTEND";

    /**
     * 출석 퀘스트 실행.
     * @return 지급된 코인 (이미 출석했으면 0)
     */
    @Transactional
    public int attend(Integer userId) {
        // 1) 사용자 조회
        User user = userRepo.findById(userId)
            .orElseThrow(() -> new IllegalArgumentException("유저를 찾을 수 없음"));

        // 2) 이미 오늘 출석했는지 검사 (User.lastAttend, questCoin 필드 필요)
        LocalDate today = LocalDate.now();
        if (today.equals(user.getLastAttend())) {
            return 0;
        }

        // 3) 사용자 코인·출석일 갱신
        user.setQuestCoin(user.getQuestCoin() + REWARD);
        user.setLastAttend(today);

        // 4) 퀘스트 테이블 상태 변경
        questRepo.findByUserIdAndQuestTypeAndQuestDoneFalse(userId, TYPE)
            .ifPresent(q -> {
                q.setStarted(true);
                q.setProgress(1);
                q.setQuestDone(true);
            });

        return REWARD;
    }
}
