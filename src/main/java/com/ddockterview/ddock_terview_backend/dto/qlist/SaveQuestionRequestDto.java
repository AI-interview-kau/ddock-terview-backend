package com.ddockterview.ddock_terview_backend.dto.qlist;

import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class SaveQuestionRequestDto {
    private Long baseQuestionId;      // bq_id
    private Long interviewQuestionId; // inq_id
}
