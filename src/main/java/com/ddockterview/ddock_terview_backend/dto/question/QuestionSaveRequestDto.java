package com.ddockterview.ddock_terview_backend.dto.question;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@NoArgsConstructor
public class QuestionSaveRequestDto {
    private List<QuestionContentDto> items;
}
