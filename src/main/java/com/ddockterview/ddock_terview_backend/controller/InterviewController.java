//package com.ddockterview.ddock_terview_backend.controller;
//
//import com.ddockterview.ddock_terview_backend.dto.redis.InterviewSession;
//import com.ddockterview.ddock_terview_backend.service.S3LinkService;
//import com.ddockterview.ddock_terview_backend.service.agent.InterviewSessionService;
////import com.ddockterview.ddock_terview_backend.service.agent.VertexAiAgentService;
//import org.springframework.http.ResponseEntity;
//import org.springframework.web.bind.annotation.PostMapping;
//import org.springframework.web.bind.annotation.RequestMapping;
//import org.springframework.web.bind.annotation.RequestParam;
//import org.springframework.web.bind.annotation.RestController;
//import org.springframework.web.multipart.MultipartFile;
//import reactor.core.publisher.Mono;
//
//import java.io.IOException;
//
//
//record StartResponse(String sessionId, String firstQuestion) {}
//record NextResponse(String sessionId, String nextQuestion, boolean isFinished) {}
//
//@RestController
//@RequestMapping("/api/interview")
//public class InterviewController {
//
//    private final VertexAiAgentService agentService;
//    private final S3LinkService s3Service;
//    private final InterviewSessionService sessionService;
//
//    public InterviewController(VertexAiAgentService agentService, S3LinkService s3Service,
//                               InterviewSessionService sessionService) {
//        this.agentService = agentService;
//        this.s3Service = s3Service;
//        this.sessionService = sessionService;
//    }
//
//    @PostMapping(value = "/start", consumes = "multipart/form-data")
//    public Mono<ResponseEntity<StartResponse>> startInterview(
//            @RequestParam("resumeFile") MultipartFile resumeFile,
//            @RequestParam("userId") Long userId) {
//
//        try {
//            // 1. PDF를 S3에 업로드하고 Pre-signed URL 받기
//            String resumeS3Url = s3Service.getDownloadUrl(resumeFile, "resumes");
//
//            // 2. (PdfParsingService 삭제됨)
//
//            // 3. Agent 1 호출 (PDF의 S3 URL 전달)
//            return agentService.callResumeAgent(resumeS3Url)
//                    .flatMap(mainQuestions -> {
//                        if (mainQuestions == null || mainQuestions.isEmpty()) {
//                            return Mono.error(new RuntimeException("AI가 질문을 생성하지 못했습니다."));
//                        }
//
//                        // 4. Redis에 세션 생성 (PDF 텍스트 대신 S3 URL 저장)
//                        InterviewSession session = sessionService.createSession(userId, resumeS3Url, mainQuestions);
//
//                        // 5. 첫 질문 가져와서 이력에 저장
//                        String firstQuestion = session.getCurrentMainQuestion();
//                        session.addTurn("AI", firstQuestion);
//                        sessionService.updateSession(session); // Redis 저장
//
//                        // 6. 프론트에 sessionId와 첫 질문 반환
//                        StartResponse response = new StartResponse(session.getSessionId(), firstQuestion);
//                        return Mono.just(ResponseEntity.ok(response));
//                    });
//
//        } catch (IOException e) {
//            return Mono.just(ResponseEntity.status(500).build()); // S3 업로드 실패
//        }
//    }
//
//}
