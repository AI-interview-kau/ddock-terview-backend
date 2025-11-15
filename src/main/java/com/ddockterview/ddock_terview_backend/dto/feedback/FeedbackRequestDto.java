package com.ddockterview.ddock_terview_backend.dto.feedback;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
public class FeedbackRequestDto {
    private String userId;
    private LocalDateTime createdAt;
    private List<QuestionFeedbackDto> questions;

    @JsonProperty("general_feedback")
    private String generalFeedback;

    private Integer totalScore;
    private Integer suitability;
    private Integer intendunderstanding;
    private Integer problemsolving;
    private Integer accuracy;
    private Integer experience;
    private Integer logicality;
    private Integer confidence;
    private Integer speed;
    private Integer voice;
    private Integer gesture;
    private Integer attitude;
    private Integer gazing;
    private String pro;
    private String con;
}
