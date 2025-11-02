package com.ddockterview.ddock_terview_backend.dto.feedbackPerQ;

import com.ddockterview.ddock_terview_backend.entity.FeedbackPerQ;
import com.ddockterview.ddock_terview_backend.entity.QuestionAfter;
import lombok.Getter;

@Getter
public class QuestionFeedbackDetailDto {

    private String content;
    private String s3key;
    private String behavefeedback;
    private String langfeedback;

    public QuestionFeedbackDetailDto(QuestionAfter question, FeedbackPerQ feedback) {
        this.content = question.getContent();
        this.s3key = question.getS3key();

        if (feedback != null) {
            this.behavefeedback = feedback.getBehavefeedback();
            this.langfeedback = feedback.getLangfeedback();
        } else {
            // 피드백이 없는 경우, 빈 문자열이나 기본 메시지 처리
            this.behavefeedback = "생성된 행동 피드백이 없습니다.";
            this.langfeedback = "생성된 언어 피드백이 없습니다.";
        }
    }

}
