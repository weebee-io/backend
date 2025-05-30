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

    @Builder.Default
    @Column(name = "coin", nullable = false)
    private int coin = 0;

    /* ──────────────────────────────────────────────── */

    /**
     * 코인 잔액을 설정한다.
     */
    public void setCoin(int coin) {
        this.coin = coin;
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

    /**
     * User는 직접 statSum을 가지고 있지 않으므로,
     * 이 메서드를 호출할 때는 주의가 필요합니다.
     * JSON 직렬화 시 이 메서드가 호출될 수 있으므로
     * 예외 대신 기본값 0을 반환합니다.
     */
    public int getStatSum() {
        return 0; // 기본값 반환
    }
}