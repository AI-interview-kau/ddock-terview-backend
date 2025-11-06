package com.ddockterview.ddock_terview_backend.dto.savedQ;

import lombok.Getter;

import java.util.List;

@Getter
public class SaveQuestionListDto {

    private List<String> contents;

    public SaveQuestionListDto(List<String> contents) {
        this.contents = contents;
    }
}
