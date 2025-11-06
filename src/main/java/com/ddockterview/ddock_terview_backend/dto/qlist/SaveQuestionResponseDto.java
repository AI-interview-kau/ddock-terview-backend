package com.ddockterview.ddock_terview_backend.dto.qlist;

import com.ddockterview.ddock_terview_backend.entity.SavedQuestion;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
public class SaveQuestionResponseDto {

    private Long sqId;
    private String userId;
    private Long bqId;
    private Long inqId;
    private LocalDateTime createdAt;

    public SaveQuestionResponseDto(SavedQuestion savedQuestion) {
        this.sqId = savedQuestion.getSqId();
        this.userId = savedQuestion.getUser().getUserId();
        this.bqId = (savedQuestion.getBaseQuestion() != null) ? savedQuestion.getBaseQuestion().getBqId() : null;
        this.inqId = (savedQuestion.getQuestionAfter() != null) ? savedQuestion.getQuestionAfter().getInqId() : null;
        this.createdAt = savedQuestion.getCreatedAt();
    }


}
