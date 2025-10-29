package com.ddockterview.ddock_terview_backend.jwt;

import com.ddockterview.ddock_terview_backend.dto.login.JwtToken;
import io.jsonwebtoken.*;
import io.jsonwebtoken.security.Keys;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.stereotype.Component;

import java.security.Key;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.Date;
import java.util.stream.Collectors;

@Slf4j
@Component
public class JwtTokenProvider {

    private final Key key;
    private final UserDetailsService userDetailsService;

    // application.yml 등에서 비밀키를 가져옵니다.
    public JwtTokenProvider(@Value("${jwt.secret}") String secretKey, UserDetailsService userDetailsService) {
        byte[] keyBytes = secretKey.getBytes();
        this.key = Keys.hmacShaKeyFor(keyBytes);
        this.userDetailsService = userDetailsService;
    }

    // Access Token 생성
    public JwtToken generateToken(Authentication authentication) {
        // 권한 가져오기
        String authorities = authentication.getAuthorities().stream()
                .map(GrantedAuthority::getAuthority)
                .collect(Collectors.joining(","));

        long now = (new Date()).getTime();
        // 토큰 만료 시간 설정
        Date accessTokenExpiresIn = new Date(now + 86400000);

        // 1. Access Token(String) 생성
        String accessToken = Jwts.builder()
                .setSubject(authentication.getName())
                .claim("roles", authorities)
                .setExpiration(accessTokenExpiresIn)
                .signWith(key, SignatureAlgorithm.HS256)
                .compact();

        String refreshToken = Jwts.builder()
                .setExpiration(new Date(now + 86400000))
                .signWith(key, SignatureAlgorithm.HS256)
                .compact();

        // 2. 생성된 토큰을 TokenInfo 객체에 담아 반환
        return JwtToken.builder()
                .grantType("Bearer")
                .accessToken(accessToken)
                .refreshToken(refreshToken)
                .build();
    }

    // 토큰에서 인증 정보 조회
    public Authentication getAuthentication(String accessToken) {
        Claims claims = parseClaims(accessToken);

        String roles = (String) claims.get("roles");
        Collection<? extends GrantedAuthority> authorities;

        if(roles == null || roles.isEmpty()) {
            authorities = Collections.emptyList();
        }else {
            authorities = Arrays.stream(roles.split(","))
                    .filter(role -> !role.isEmpty())
                    .map(SimpleGrantedAuthority::new)
                    .collect(Collectors.toList());
        }

        String userId = claims.getSubject();
        UserDetails userDetails = userDetailsService.loadUserByUsername(userId);

        return new UsernamePasswordAuthenticationToken(userDetails, "", authorities);
    }

    // 토큰 유효성 검증
    public boolean validateToken(String token) {
        try {
            Jwts.parserBuilder().setSigningKey(key).build().parseClaimsJws(token);
            return true;
        } catch (SecurityException | MalformedJwtException e) {
            // 잘못된 JWT 서명
            log.info("Invalid JWT token", e);
        } catch (ExpiredJwtException e) {
            // 만료된 JWT
            log.info("Expired JWT Token",e);
        } catch (UnsupportedJwtException e) {
            // 지원되지 않는 JWT
            log.info("Unsupported JWT token", e);
        } catch (IllegalArgumentException e) {
            // JWT 클레임 문자열이 비어 있음
            log.info("JWT claims string is empty", e);
        }
        return false;
    }

    private Claims parseClaims(String token) {
        try {
            return Jwts.parserBuilder()
                    .setSigningKey(key)
                    .build()
                    .parseClaimsJws(token)
                    .getBody();
        } catch (Exception e) {
            // 토큰 파싱 중 발생하는 예외 처리 (예: ExpiredJwtException)
            // 실제로는 예외 유형에 따라 다르게 처리해야 할 수 있습니다.
            throw new IllegalArgumentException("유효하지 않은 토큰입니다.", e);
        }
    }
}
