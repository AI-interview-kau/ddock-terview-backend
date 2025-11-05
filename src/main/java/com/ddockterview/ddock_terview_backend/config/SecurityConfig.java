package com.ddockterview.ddock_terview_backend.config;

import com.ddockterview.ddock_terview_backend.jwt.JwtAuthenticationFilter;
import com.ddockterview.ddock_terview_backend.jwt.JwtTokenProvider;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.factory.PasswordEncoderFactories;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
public class SecurityConfig {

    private final JwtTokenProvider jwtTokenProvider;


    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http
                // 1. httpBasic, csrf 설정 변경
                .httpBasic(basic -> basic.disable())
                .csrf(csrf -> csrf.disable())

                // 2. 세션 관리 설정 변경
                .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))

                // 3. .and() 대신 람다식으로 바로 권한 설정
                .authorizeHttpRequests(authorize -> authorize
                        .requestMatchers(
                                "/user/login",
                                "/user/join",
                                "/swagger-ui/**",
                                "/v3/api-docs/**",
                                "/session/**"
                        ).permitAll() // 허용할 경로
                        .anyRequest().authenticated() // 나머지 경로는 인증 필요
                )

                // 4. 필터 추가 (.and() 없음)
                .addFilterBefore(new JwtAuthenticationFilter(jwtTokenProvider), UsernamePasswordAuthenticationFilter.class);

        return http.build();
    }

    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration authenticationConfiguration) throws Exception {
        return authenticationConfiguration.getAuthenticationManager();
    }


}
