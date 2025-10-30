package com.ddockterview.ddock_terview_backend.dto.qlist;

import lombok.Getter;

import java.util.List;

@Getter
public class QuestionCategoryResponseDto {

    private List<QuestionItemDto> items;

    public QuestionCategoryResponseDto(List<QuestionItemDto> items) {
        this.items = items;
    }

}
