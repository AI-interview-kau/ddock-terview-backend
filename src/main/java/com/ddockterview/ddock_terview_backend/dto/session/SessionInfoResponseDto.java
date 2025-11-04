//package com.ddockterview.ddock_terview_backend.dto.session;
//
//import com.ddockterview.ddock_terview_backend.entity.Session;
//import com.ddockterview.ddock_terview_backend.entity.User;
//import com.ddockterview.ddock_terview_backend.entity.enums.InterviewType;
//import com.ddockterview.ddock_terview_backend.entity.enums.InterviewerType;
//
//import java.time.LocalDateTime;
//
//public class SessionInfoResponseDto {
//
//    private Long sessionId;
//    private InterviewType interviewType;
//    private InterviewerType interviewerType;
//    private String userId;
//    private LocalDateTime createdAt;
//
//    public SessionInfoResponseDto(Session session) {
//        this.sessionId = session.getSessionId();
//        this.interviewType = session.getInterviewType();
//        this.interviewerType = session.getInterviewerType();
//        this.userId = session.getSessionUser().getUserId();
//        this.createdAt = session.getCreatedAt();
//    }
//
//
//}
