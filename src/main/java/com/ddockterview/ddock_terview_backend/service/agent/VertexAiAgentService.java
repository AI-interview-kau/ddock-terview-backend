//package com.ddockterview.ddock_terview_backend.service.agent;
//
//import com.ddockterview.ddock_terview_backend.dto.redis.Turn;
//import com.google.api.client.util.Value;
//import com.google.auth.oauth2.GoogleCredentials;
//import org.springframework.http.HttpHeaders;
//import org.springframework.http.MediaType;
//import org.springframework.web.reactive.function.client.WebClient;
//import reactor.core.publisher.Mono;
//
//import java.io.IOException;
//import java.util.Collections;
//import java.util.List;
//import java.util.Map;
//
//public class VertexAiAgentService {
//    private final WebClient webClient;
//    private final String resumeAgentUrl;
//    private final String sessionAgentUrl;
//
//    public VertexAiAgentService(WebClient.Builder builder,
//                                @Value("${vertex.agent.url.resume}") String resumeAgentUrl,
//                                @Value("${vertex.agent.url.session}") String sessionAgentUrl) {
//        this.webClient = builder.build();
//        this.resumeAgentUrl = resumeAgentUrl;
//        this.sessionAgentUrl = sessionAgentUrl;
//    }
//
//    private String getAccessToken() throws IOException {
//        GoogleCredentials credentials = GoogleCredentials.getApplicationDefault()
//                .createScoped(Collections.singletonList("https://www.googleapis.com/auth/cloud-platform"));
//        credentials.refreshIfExpired();
//        return credentials.getAccessToken().getTokenValue();
//    }
//
//    /**
//     * Agent 1: 자소서 텍스트로 메인 질문 리스트 요청
//     */
//    public Mono<List<String>> callResumeAgent(String resumeS3Url) {
//        // (가정) 스키마: {"input": {"resume_text": "..."}}
//        // (응답) 스키마: {"output": {"questions": ["q1", "q2", ...]}}
//        Map<String, Object> requestBody = Map.of("input", Map.of("resume_url", resumeS3Url));
//
//        return callAgentApi(resumeAgentUrl, requestBody)
//                .map(response -> (List<String>) ((Map<String, Object>) response.get("output")).get("questions"));
//    }
//
//    /**
//     * Agent 2: 꼬리질문 여부/내용 요청
//     */
//    public Mono<Map<String, Object>> callSessionAgent(String resumeS3Url, List<Turn> history) {
//        // (가정) 스키마: {"input": {"history": [Turn, Turn, ...]}}
//        // (응답) 스키마: {"output": {"hasFollowUp": true, "question": "..."}}
//        Map<String, Object> requestBody = Map.of("input", Map.of(
//                "resume_url", resumeS3Url,
//                "history", history));
//
//        return callAgentApi(sessionAgentUrl, requestBody)
//                .map(response -> (Map<String, Object>) response.get("output"));
//    }
//
//    // 공통 API 호출 로직
//    private Mono<Map<String, Object>> callAgentApi(String url, Map<String, Object> requestBody) {
//        try {
//            String token = getAccessToken();
//            return webClient.post()
//                    .uri(url)
//                    .header(HttpHeaders.AUTHORIZATION, "Bearer " + token)
//                    .contentType(MediaType.APPLICATION_JSON)
//                    .bodyValue(requestBody)
//                    .retrieve()
//                    .bodyToMono(Map.class)
//                    .doOnError(e -> System.err.println("AI Agent Call Failed: " + e.getMessage()));
//        } catch (IOException e) {
//            return Mono.error(new RuntimeException("GCP Auth Failed", e));
//        }
//    }
//
//
//}
