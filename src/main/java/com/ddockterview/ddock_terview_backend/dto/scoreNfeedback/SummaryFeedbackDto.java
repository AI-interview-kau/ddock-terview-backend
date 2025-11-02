package com.ddockterview.ddock_terview_backend.dto.scoreNfeedback;

import com.ddockterview.ddock_terview_backend.entity.ScoreAndFeedback;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;

@Getter
public class SummaryFeedbackDto {

    private String general;

    @JsonProperty("pros")
    private String pros;

    @JsonProperty("cons")
    private String cons;

    public SummaryFeedbackDto(ScoreAndFeedback feedback) {
        this.general = feedback.getGeneralFeedback();
        this.pros = feedback.getPro();
        this.cons = feedback.getCon();
    }


}
