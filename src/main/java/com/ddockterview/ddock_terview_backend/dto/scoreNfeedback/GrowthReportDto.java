package com.ddockterview.ddock_terview_backend.dto.scoreNfeedback;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.List;

@Getter
@AllArgsConstructor
public class GrowthReportDto {

    private List<String> labels;
    private List<Integer> scores;
}
