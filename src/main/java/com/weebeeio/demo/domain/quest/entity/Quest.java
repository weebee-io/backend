package com.weebeeio.demo.domain.quest.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Entity
@Table(name = "quest")
@Getter @Setter
public class Quest {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "quest_id")
    private Integer questId;

    @Column(name = "user_id", nullable = false)
    private Integer userId;

    /** 'QUIZ', 'ATTEND' 등 */
    @Column(name = "quest_type", length = 20, nullable = false)
    private String questType;

    /** 목표 수행 횟수 (출석은 1) */
    @Column(name = "target_cnt", nullable = false)
    private Integer targetCnt;

    /** 현재 수행도 */
    @Column(name = "progress", nullable = false)
    private Integer progress;

    /** 시작 버튼 클릭 여부 */
    @Column(name = "started", nullable = false)
    private Boolean started;

    /** 완료 여부 */
    @Column(name = "quest_done", nullable = false)
    private Boolean questDone;
}
