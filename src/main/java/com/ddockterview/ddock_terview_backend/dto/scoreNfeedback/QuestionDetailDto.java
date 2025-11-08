package com.ddockterview.ddock_terview_backend.dto.scoreNfeedback;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class QuestionDetailDto {
    private Long inqId;
    private String question;
    private Boolean isTailQ;
}
