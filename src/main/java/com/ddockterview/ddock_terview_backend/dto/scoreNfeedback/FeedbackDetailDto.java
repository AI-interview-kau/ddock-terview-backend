package com.ddockterview.ddock_terview_backend.dto.scoreNfeedback;

import com.ddockterview.ddock_terview_backend.entity.ScoreAndFeedback;
import lombok.Getter;

@Getter
public class FeedbackDetailDto {

    private int totalScore;
    private String generalFeedback;
    private String pro;
    private String con;

    public FeedbackDetailDto(ScoreAndFeedback feedback) {
        this.totalScore = feedback.getTotalScore();
        this.generalFeedback = feedback.getGeneralFeedback();
        this.pro = feedback.getPro();
        this.con = feedback.getCon();
    }

    public FeedbackDetailDto() {
        this.totalScore = 0;
        this.generalFeedback = null;
        this.pro = null;
        this.con = null;
    }

}
