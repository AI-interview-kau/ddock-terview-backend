//package com.ddockterview.ddock_terview_backend.service.agent;
//
////import com.google.auth.oauth2.AccessToken;
////import com.google.auth.oauth2.GoogleCredentials;
//import org.springframework.http.HttpHeaders;
//import org.springframework.http.MediaType;
//import org.springframework.stereotype.Service;
//import org.springframework.web.reactive.function.client.WebClient;
//import reactor.core.publisher.Mono;
//
//import java.io.IOException;
//import java.util.Collections;
//import java.util.Map;
//
//@Service
//public class ResumeAgentService {
//
//    private final WebClient webClient;
//
//    private final String agentUrl = "https://us-central1-aiplatform.googleapis.com/v1/projects/299282571203/locations/us-central1/reasoningEngines/1530929204189724672:query";
//
//    public ResumeAgentService(WebClient.Builder webClientBuilder) {
//        this.webClient = webClientBuilder.build();
//    }
//
//    private String getAccessToken() throws IOException {
//        // GOOGLE_APPLICATION_CREDENTIALS 환경 변수를 자동으로 읽어옵니다.
//        GoogleCredentials credentials = GoogleCredentials.getApplicationDefault()
//                .createScoped(Collections.singletonList("https://www.googleapis.com/auth/cloud-platform"));
//
//        credentials.refreshIfExpired();
//        AccessToken accessToken = credentials.getAccessToken();
//
//        return accessToken.getTokenValue();
//    }
//
//    /**
//     * 자소서 텍스트를 Vertex AI 에이전트로 보내고 질문을 받아옵니다.
//     * @param resumeText 프론트엔드에서 받은 자소서 본문
//     * @return AI가 생성한 질문 (JSON 응답을 Map으로 변환)
//     */
//    public Mono<Map> generateQuestions(String resumeText) {
//
//        try {
//            // 1. 인증 토큰 가져오기
//            String token = getAccessToken();
//
//            // 2. AI 에이전트에 보낼 JSON 본문(Body) 구성
//            // (중요) Vertex AI에서 설정한 입력 스키마에 맞게 수정해야 합니다.
//            // 저는 {"input": {"resume_text": "..."}} 라고 가정했습니다.
//            Map<String, Object> requestBody = Map.of(
//                    "input", Map.of("resume_text", resumeText)
//            );
//
//            // 3. WebClient로 API 호출
//            return this.webClient.post()
//                    .uri(agentUrl)
//                    .header(HttpHeaders.AUTHORIZATION, "Bearer " + token)
//                    .contentType(MediaType.APPLICATION_JSON)
//                    .bodyValue(requestBody) // 요청 본문
//                    .retrieve() // 요청 실행
//                    .bodyToMono(Map.class) // 응답을 Map (JSON)으로 받음
//                    .doOnSuccess(response -> System.out.println("AI 응답 성공: " + response))
//                    .doOnError(e -> System.err.println("AI 호출 실패: " + e.getMessage()));
//
//        } catch (IOException e) {
//            // 인증 자체에 실패한 경우 (e.g., 서비스 계정 키 파일을 못 찾음)
//            System.err.println("Google 인증 실패: " + e.getMessage());
//            return Mono.error(new RuntimeException("Google 인증에 실패했습니다.", e));
//        }
//    }
//}
