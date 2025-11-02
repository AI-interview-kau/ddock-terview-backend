package com.ddockterview.ddock_terview_backend.dto.scoreNfeedback;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.List;

@Getter
@AllArgsConstructor
public class LogDetailResponseDto {

    private FeedbackDetailDto feedback;
    private List<String> questions;

}
