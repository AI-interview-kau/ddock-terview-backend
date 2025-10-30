package com.ddockterview.ddock_terview_backend.dto.scoreNfeedback;

import com.ddockterview.ddock_terview_backend.entity.ScoreAndFeedback;
import com.ddockterview.ddock_terview_backend.entity.Session;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
public class SessionLogDto {

    private Long sessionId;
    private LocalDateTime date;
    private int index;
    private int totalScore;
    private SummaryFeedbackDto summaryFeedback;
    private DetailScoreDto detailScore;

    public SessionLogDto(Session session, ScoreAndFeedback feedback, int index) {
        this.sessionId = session.getSessionId();
        this.date = session.getCreatedAt(); // 세션 생성 시간을 기준으로 함
        this.index = index;

        // 피드백이 아직 없을 수도 있으므로 null 체크
        if (feedback != null) {
            this.totalScore = feedback.getTotalScore();
            this.summaryFeedback = new SummaryFeedbackDto(feedback);
            this.detailScore = new DetailScoreDto(feedback);
        } else {
            // 피드백이 없는 경우 기본값 처리
            this.totalScore = 0;
            this.summaryFeedback = null; // 또는 기본 DTO
            this.detailScore = null;   // 또는 기본 DTO
        }
    }

}
