package com.ddockterview.ddock_terview_backend.dto.qlist;

import lombok.Getter;

import java.util.Map;

@Getter
public class QuestionListResponseDto {

    private Map<String, QuestionCategoryResponseDto> categories;
    public QuestionListResponseDto(Map<String, QuestionCategoryResponseDto> categories) {
        this.categories = categories;
    }

}
