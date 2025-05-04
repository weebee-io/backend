package com.weebeeio.demo.domain.login.entity;

import java.time.LocalDate;
import java.util.List;
import com.weebeeio.demo.domain.quiz.dao.QuizResultDao;
import jakarta.persistence.*;
import lombok.*;

/**
 * 사용자 엔티티
 */
@Entity
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Table(name = "users")
public class User {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "user_id")
    private Integer userId;

    @Column(nullable = false, length = 45, unique = true)
    private String id;

    @Column(nullable = false, length = 45)
    private String password;

    @Column(length = 45, unique = true)
    private String nickname;

    @Column(length = 45)
    private String name;

    @Column(length = 45)
    private String gender;

    @Column(name = "user_rank", nullable = true)
    private String userrank;

    @Column
    private Integer age;

    @Column(name = "user_segment", length = 45, nullable = true)
    private String userSegment;

    @OneToMany(mappedBy = "user", fetch = FetchType.LAZY)
    private List<QuizResultDao> quizResults;

    /* ──────────────────────────────────────────────── */
    /** 최종 출석 일자 */
    @Column(name = "last_attend")
    private LocalDate lastAttend;

    /** 퀴즈 점수 (랭크·리더보드용) */
    @Builder.Default
    @Column(name = "quiz_point", nullable = false)
    private int quizPoint = 0;

    /** 퀘스트 코인 (상점·출석 보상용) */
    @Builder.Default
    @Column(name = "quest_coin", nullable = false)
    private int questCoin = 0;
    /* ──────────────────────────────────────────────── */

    /**
     * 퀘스트 코인 잔액을 설정한다.
     */
    public void setQuestCoin(int questCoin) {
        this.questCoin = questCoin;
    }

    /**
     * 최종 출석 일자를 설정한다.
     */
    public void setLastAttend(LocalDate lastAttend) {
        this.lastAttend = lastAttend;
    }
}
