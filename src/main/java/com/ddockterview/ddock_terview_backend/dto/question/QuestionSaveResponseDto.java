package com.ddockterview.ddock_terview_backend.dto.question;

import com.ddockterview.ddock_terview_backend.entity.QuestionAfter;
import com.ddockterview.ddock_terview_backend.entity.Session;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;

import java.util.List;
import java.util.stream.Collectors;

@Getter
public class QuestionSaveResponseDto {

    @JsonProperty("session_id")
    private Long sessionId;

    @JsonProperty("user_Id")
    private String userId;

    private List<QuestionSaveResponseItemDto> items;

    public QuestionSaveResponseDto(Session session, List<QuestionAfter> savedQuestions) {
        this.sessionId = session.getSessionId();
        this.userId = session.getSessionUser().getUserId(); // User 엔티티의 ID getter
        this.items = savedQuestions.stream()
                .map(QuestionSaveResponseItemDto::new) // .map(qa -> new QuestionSaveResponseItemDto(qa))
                .collect(Collectors.toList());
    }

}
