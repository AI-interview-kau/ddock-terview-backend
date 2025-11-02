package com.ddockterview.ddock_terview_backend.dto.question;

import com.ddockterview.ddock_terview_backend.entity.QuestionAfter;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
public class QuestionSaveResponseItemDto {

    @JsonProperty("inq_id")
    private Long inqId;
    private String content;
    private LocalDateTime createdAt;

    public QuestionSaveResponseItemDto(QuestionAfter qa) {
        this.inqId = qa.getInqId();
        this.content = qa.getContent();
        this.createdAt = qa.getCreatedAt();
    }

}
