package com.ddockterview.ddock_terview_backend.dto.qlist;

import com.ddockterview.ddock_terview_backend.entity.BaseQuestion;
import com.ddockterview.ddock_terview_backend.entity.User;
import com.ddockterview.ddock_terview_backend.entity.enums.Category;
import com.ddockterview.ddock_terview_backend.entity.enums.Origin;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class MyQuestionRequestDto {

    private Category category;
    private String content;

    public BaseQuestion toEntity(User user) {
        return BaseQuestion.builder()
                .user(user)
                .category(this.category)
                .content(this.content)
                .origin(Origin.USER)
                .build();
    }
}
