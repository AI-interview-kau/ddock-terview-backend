package com.ddockterview.ddock_terview_backend.dto.qlist;

import com.ddockterview.ddock_terview_backend.entity.BaseQuestion;
import com.ddockterview.ddock_terview_backend.entity.User;
import com.ddockterview.ddock_terview_backend.entity.enums.Origin;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
public class MyQuestionResponseDto {

    private Long bqId;
    private String userId;
    private String content;
    private Origin origin;
    private LocalDateTime createdAt;

    public MyQuestionResponseDto(BaseQuestion question) {
        this.bqId = question.getBqId();
        this.userId = question.getUser().getUserId();
        this.content = question.getContent();
        this.origin = question.getOrigin();
        this.createdAt = question.getCreatedAt();
    }

}
