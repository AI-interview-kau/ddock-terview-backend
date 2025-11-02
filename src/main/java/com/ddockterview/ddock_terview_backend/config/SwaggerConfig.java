package com.ddockterview.ddock_terview_backend.config;

import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.Components;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.security.SecurityRequirement;
import io.swagger.v3.oas.models.security.SecurityScheme;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class SwaggerConfig {

    @Bean
    public OpenAPI openAPI() {
        // 1. API 문서의 기본 정보 (제목, 설명, 버전) 설정
        Info info = new Info()
                .title("Ddock-terview API Document") // (프로젝트 이름으로 수정)
                .version("v1.0.0")
                .description("똑터뷰 프로젝트의 API 명세서입니다.");

        // 2. JWT 인증 처리를 위한 SecurityScheme 설정 (이름은 "Bearer Authentication")
        String jwtSchemeName = "Bearer Authentication";
        SecurityRequirement securityRequirement = new SecurityRequirement().addList(jwtSchemeName);

        Components components = new Components()
                .addSecuritySchemes(jwtSchemeName, new SecurityScheme()
                        .name(jwtSchemeName)
                        .type(SecurityScheme.Type.HTTP) // HTTP 방식
                        .scheme("bearer")               // Bearer 토큰 사용
                        .bearerFormat("JWT"));          // JWT 포맷

        // 3. OpenAPI 객체 생성 및 반환
        return new OpenAPI()
                .info(info)
                .addSecurityItem(securityRequirement) // SecurityRequirement 추가
                .components(components);              // Components 추가
    }

}
