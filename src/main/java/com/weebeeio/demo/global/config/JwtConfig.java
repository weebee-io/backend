package com.weebeeio.demo.global.config;

import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import javax.crypto.SecretKey;
import java.nio.charset.StandardCharsets;
import java.util.List;

@Configuration
public class JwtConfig {

    @Value("${jwt.secret}")
    private String secretKey;

    private static final List<String> EXCLUDE_URLS = List.of(
        "/users/signup",
        "/users/login",
        "/users/check-id",
        "/swagger-ui",
        "/swagger-ui/",
        "/swagger-ui.html",
        "/v3/api-docs",
        "/v3/api-docs/",
        "/webjars"
    );

    @Bean
    public SecretKey secretKey() {
        return Keys.hmacShaKeyFor(secretKey.getBytes(StandardCharsets.UTF_8));
    }
} 