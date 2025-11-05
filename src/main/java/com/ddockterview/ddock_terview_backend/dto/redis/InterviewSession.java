package com.ddockterview.ddock_terview_backend.dto.redis;

import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Data
@NoArgsConstructor
public class InterviewSession implements Serializable {

    private String sessionId;
    private Long userId;

    //AI가 맥락을 파악할 자소서의 S3 URL
    private String resumeS3Url;

    // Agent 1이 생성한 메인 질문 리스트
    private List<String> mainQuestions;

    // 메인 질문 북마크
    private int mainQuestionIndex;

    // 전체 대화 이력
    private List<Turn> conversationHistory;

    //(Key: 질문, Value: 답변 S3 URL)
    private Map<String, String> answerUrls;

    public InterviewSession(String sessionId, Long userId, String resumeS3Url, List<String> mainQuestions) {
        this.sessionId = sessionId;
        this.userId = userId;
        this.resumeS3Url = resumeS3Url; // PDF 텍스트 대신 URL 저장
        this.mainQuestions = mainQuestions;
        this.mainQuestionIndex = 0;
        this.conversationHistory = new ArrayList<>();
        this.answerUrls = new HashMap<>();
    }

    public String getCurrentMainQuestion() {
        if (isMainQuestionsFinished()) {
            return null;
        }
        return mainQuestions.get(mainQuestionIndex);
    }

    public void moveToNextMainQuestion() {
        if (!isMainQuestionsFinished()) {
            this.mainQuestionIndex++;
        }
    }

    public boolean isMainQuestionsFinished() {
        return mainQuestionIndex >= mainQuestions.size();
    }

    public void addTurn(String speaker, String content) {
        this.conversationHistory.add(new Turn(speaker, content));
    }

    public void addAnswer(String question, String s3Url) {
        this.answerUrls.put(question, s3Url);
    }

}
