package com.ddockterview.ddock_terview_backend.dto.session;

import com.ddockterview.ddock_terview_backend.entity.Session;
import com.ddockterview.ddock_terview_backend.entity.enums.InterviewType;
import com.ddockterview.ddock_terview_backend.entity.enums.InterviewerType;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
public class SessionResponseDto {

    private Long sessionId;
    private String userId;
    private InterviewType interviewType;
    private InterviewerType interviewerType;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

    // Entity를 DTO로 변환하는 생성자
    public SessionResponseDto(Session session) {
        this.sessionId = session.getSessionId();
        this.userId = session.getSessionUser().getUserId();
        this.interviewType = session.getInterviewType();
        this.interviewerType = session.getInterviewerType();
        this.createdAt = session.getCreatedAt();
        this.updatedAt = session.getUpdatedAt();
    }

}
