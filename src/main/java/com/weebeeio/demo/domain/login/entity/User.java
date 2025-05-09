package com.weebeeio.demo.domain.login.entity;

import java.time.LocalDate;
import java.util.Collection;
import java.util.List;
import com.weebeeio.demo.domain.quiz.dao.QuizResultDao;
import jakarta.persistence.*;
import lombok.*;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;


/**
 * 사용자 엔티티
 */
@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Table(name = "users")
public class User implements UserDetails {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "user_id")
    private Integer userId;

    @Column(nullable = false, length = 45, unique = true)
    private String id;
    
    @Column(name = "password", length = 100, nullable = false)
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

    // UserDetails 인터페이스 구현
    @Override // 권한 반환
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return List.of(new SimpleGrantedAuthority("user"));
    }

    @Override  // 유저 id 반환환
    public String getUsername() {
        return this.id;
    }

    @Override  // 유저 패스워드 반환환
    public String getPassword() {
        return password;
    }

    @Override  //계정 만료 여부 반환환
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override // 계정 잠금 여부 반환환
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override // 패스워드의 만료 여부 반환환
    public boolean isCredentialsNonExpired() {
        // 패스워드가 만료 되었는지 확인하는 로직
        return true; // true -> 만료되지 않음.
    }

    @Override // 계정 사용 가능 여부 반환환
    public boolean isEnabled() {
        // 계정 사용 가능한지 확인하는 로직직
        return true; // ture -> 사용 가능
    }
}