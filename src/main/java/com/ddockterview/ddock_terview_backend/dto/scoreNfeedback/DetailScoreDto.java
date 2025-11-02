package com.ddockterview.ddock_terview_backend.dto.scoreNfeedback;

import com.ddockterview.ddock_terview_backend.entity.ScoreAndFeedback;
import lombok.Getter;

@Getter
public class DetailScoreDto {
    private int suitability;
    private int intendUnderstanding;
    private int problemSolving;
    private int accuracy;
    private int experience;
    private int logicality;
    private int confidence;
    private int speed;
    private int voice;
    private int gesture;
    private int attitude;
    private int gazing;

    public DetailScoreDto(ScoreAndFeedback feedback) {
        this.suitability = feedback.getSuitability();
        this.intendUnderstanding = feedback.getIntendUnderstanding();
        this.problemSolving = feedback.getProblemSolving();
        this.accuracy = feedback.getAccuracy();
        this.experience = feedback.getExperience();
        this.logicality = feedback.getLogicality();
        this.confidence = feedback.getConfidence();
        this.speed = feedback.getSpeed();
        this.voice = feedback.getVoice();
        this.gesture = feedback.getGesture();
        this.attitude = feedback.getAttitude();
        this.gazing = feedback.getGazing();
    }

}
