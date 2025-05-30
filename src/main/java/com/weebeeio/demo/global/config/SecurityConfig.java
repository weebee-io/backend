package com.weebeeio.demo.global.config;

import com.weebeeio.demo.domain.login.service.CustomUserDetailsService;
import com.weebeeio.demo.global.filter.JwtAuthenticationFilter;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;
import org.springframework.web.filter.CorsFilter;

import static org.springframework.security.config.Customizer.withDefaults;

@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
public class SecurityConfig {

    private final JwtAuthenticationFilter jwtAuthenticationFilter;
    private final CustomUserDetailsService customUserDetailsService;

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
                // 1) CORS 설정 (CorsFilter Bean 과 연계)
                .cors(withDefaults())

                // 2) CSRF 비활성화 (REST API 이므로)
                .csrf(csrf -> csrf.disable())

                // 3) 인증/인가 설정
                .authorizeHttpRequests(auth -> auth
                        // Swagger/OpenAPI
                        .requestMatchers(
                                "/v3/api-docs/**",
                                "/swagger-ui/**",
                                "/swagger-ui/index.html#",
                                "/swagger-ui.html",
                                "/webjars/**"
                        ).permitAll()

                        // 회원가입·로그인 등 인증 없이 접근할 API
                        .requestMatchers(
                                "/users/signup",
                                "/users/login",
                                "/users/check-id/**",
                                "/quiz/admin/upload"
                        ).permitAll()

                        // 설문 제출 API (POST /surveys) – 인증 없이 허용
                        .requestMatchers(HttpMethod.POST, "/surveys").permitAll()

                        // 스프링 시큐리티 에러 핸들러 포워딩 경로도 풀어주기
                        .requestMatchers("/error").permitAll()

                        // 그 외 모든 요청은 JWT 토큰으로만 허용
                        .anyRequest().authenticated()
                )

                // 4) 세션 사용 안 함 (stateless)
                .sessionManagement(session -> session
                        .sessionCreationPolicy(SessionCreationPolicy.STATELESS)
                )

                // 5) 인증 Provider, JWT 필터 등록
                .authenticationProvider(authenticationProvider())
                .addFilterBefore(jwtAuthenticationFilter,
                        UsernamePasswordAuthenticationFilter.class);

        return http.build();
    }

    @Bean
    public AuthenticationProvider authenticationProvider() {
        DaoAuthenticationProvider authProvider = new DaoAuthenticationProvider();
        authProvider.setUserDetailsService(customUserDetailsService);
        authProvider.setPasswordEncoder(passwordEncoder());
        return authProvider;
    }

    @Bean
    public AuthenticationManager authenticationManager(
            AuthenticationConfiguration config
    ) throws Exception {
        return config.getAuthenticationManager();
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    // CORS 설정을 전역 필터로 등록
    @Bean
    public CorsFilter corsFilter() {
        UrlBasedCorsConfigurationSource source =
                new UrlBasedCorsConfigurationSource();
        CorsConfiguration config = new CorsConfiguration();
        config.setAllowCredentials(true);
        
        // 여러 origin 허용
        config.addAllowedOrigin("http://localhost:3000"); // 로컬 프론트엔드
        config.addAllowedOrigin("http://127.0.0.1:3000"); // 로컬 IP 주소
        
        // 프로덕션 환경 프론트엔드 주소들 추가
        config.addAllowedOrigin("http://52.78.4.114:3000"); // EC2 서버 IP 주소
        
        // 개발 테스트용 와일드카드 설정 - 보안상 위험할 수 있으므로 개발 중에만 사용
        // config.addAllowedOriginPattern("*"); // 모든 origin 허용 (보안에 주의)
        
        config.addAllowedHeader("*");
        config.addAllowedMethod("*");
        config.addExposedHeader("Authorization"); // JWT 토큰 노출 허용
        
        source.registerCorsConfiguration("/**", config);
        return new CorsFilter(source);
    }
}
