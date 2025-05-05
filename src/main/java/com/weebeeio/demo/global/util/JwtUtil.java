package com.weebeeio.demo.global.util;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

import com.weebeeio.demo.domain.login.entity.User;

import java.security.Key;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;

@Component
public class JwtUtil {

    @Value("${jwt.secret}")
    private String secretKey;

    @Value("${jwt.expiration}")
    private long jwtExpiration;

    @Value("${jwt.refresh-expiration}")
    private long refreshExpiration;

    public enum TokenType {
        ACCESS, REFRESH
    }

    public String extractUsername(String token) {
        return extractClaim(token, Claims::getSubject);
    }

    public <T> T extractClaim(String token, Function<Claims, T> claimsResolver) {
        final Claims claims = extractAllClaims(token);
        return claimsResolver.apply(claims);
    }

    public String generateAccessToken(Integer userId, String username) {
        return generateToken(userId, username, TokenType.ACCESS, new HashMap<>());
    }

    public String generateRefreshToken(Integer userId, String username) {
        return generateToken(userId, username, TokenType.REFRESH, new HashMap<>());
    }

    public String generateAccessToken(Integer userId, String username, Map<String, Object> extraClaims) {
        return generateToken(userId, username, TokenType.ACCESS, extraClaims);
    }

    public String generateRefreshToken(Integer userId, String username, Map<String, Object> extraClaims) {
        return generateToken(userId, username, TokenType.REFRESH, extraClaims);
    }

    private String generateToken(Integer userId, String username, TokenType tokenType, Map<String, Object> extraClaims) {
        long expiration = tokenType == TokenType.ACCESS ? jwtExpiration : refreshExpiration;
        
        return Jwts
                .builder()
                .setClaims(extraClaims)
                .setSubject(username)
                .claim("userId", userId)
                .claim("tokenType", tokenType.name())
                .setIssuedAt(new Date(System.currentTimeMillis()))
                .setExpiration(new Date(System.currentTimeMillis() + expiration))
                .signWith(getSignInKey(), SignatureAlgorithm.HS256)
                .compact();
    }

    public boolean isTokenValid(String token, UserDetails userDetails) {
        final String username = extractUsername(token);
        return (username.equals(userDetails.getUsername())) && !isTokenExpired(token);
    }

    private boolean isTokenExpired(String token) {
        return extractExpiration(token).before(new Date());
    }

    private Date extractExpiration(String token) {
        return extractClaim(token, Claims::getExpiration);
    }

    private Claims extractAllClaims(String token) {
        return Jwts
                .parserBuilder()
                .setSigningKey(getSignInKey())
                .build()
                .parseClaimsJws(token)
                .getBody();
    }

    private Key getSignInKey() {
        byte[] keyBytes = secretKey.getBytes();
        return Keys.hmacShaKeyFor(keyBytes);
    }

    public String generateToken(User user) {
        return generateAccessToken(user.getUserId(), user.getId());
    }

    public boolean validateToken(String token) {
        try {
            Jwts.parserBuilder()
                .setSigningKey(getSignInKey())
                .build()
                .parseClaimsJws(token);
            return true;
        } catch (Exception e) {
            return false;
        }
    }
}