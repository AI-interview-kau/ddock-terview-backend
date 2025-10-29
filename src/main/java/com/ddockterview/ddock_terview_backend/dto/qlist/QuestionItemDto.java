package com.ddockterview.ddock_terview_backend.dto.qlist;

import com.ddockterview.ddock_terview_backend.entity.BaseQuestion;
import com.ddockterview.ddock_terview_backend.entity.enums.Category;
import com.ddockterview.ddock_terview_backend.entity.enums.Origin;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
@JsonInclude(JsonInclude.Include.NON_NULL)
public class QuestionItemDto {

    @JsonProperty("bq_id") // 엔티티의 id 필드를 "bq_id"로 매핑
    private Long id;

    private Category category;
    private String content;
    private Origin origin;

    @JsonProperty("user_id") // 엔티티의 user 객체에서 ID를 뽑아 "user_id"로 매핑
    private String userId;

    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

    // BaseQuestion 엔티티를 DTO로 변환하는 생성자
    public QuestionItemDto(BaseQuestion bq) {
        this.id = bq.getId();
        this.category = bq.getCategory();
        this.content = bq.getContent();
        this.origin = bq.getOrigin();
        this.createdAt = bq.getCreatedAt();
        this.updatedAt = bq.getUpdatedAt();

        // USER(내가 만든) 질문일 경우에만 userId를 설정
        if (bq.getUser() != null) {
            this.userId = bq.getUser().getUserId(); // User 엔티티에 getUserId()가 있다고 가정
        }
    }

}
