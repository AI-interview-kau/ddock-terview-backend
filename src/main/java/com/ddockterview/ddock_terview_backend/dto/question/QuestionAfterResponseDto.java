package com.ddockterview.ddock_terview_backend.dto.question;

import com.ddockterview.ddock_terview_backend.entity.QuestionAfter;
import com.ddockterview.ddock_terview_backend.entity.Session;
import com.ddockterview.ddock_terview_backend.entity.User;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
public class QuestionAfterResponseDto {

    private Long inqId;
    private Long sessionId;
    private String userId;
    private String content;
    private Boolean isTailQ;
    private String s3Key;
    private String answer;
    private LocalDateTime createdAt;

    public QuestionAfterResponseDto(QuestionAfter entity) {
        this.inqId = entity.getInqId();
        this.content = entity.getContent();
        this.isTailQ = entity.getIsTailQ();
        this.s3Key = entity.getVideoUrl();
        this.answer = entity.getAnswer();
        this.createdAt = entity.getCreatedAt();

        // 2. 연관관계 필드 처리 (NPE 방지를 위해 null 체크)
        Session session = entity.getSession();
        if (session != null) {
            this.sessionId = session.getSessionId();

            // 3. User ID는 Session을 통해 접근
            User user = session.getSessionUser();
            if (user != null) {
                this.userId = user.getUserId();
            }
        }
    }

}
