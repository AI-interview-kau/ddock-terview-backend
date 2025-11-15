package com.ddockterview.ddock_terview_backend.dto.feedback;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class QuestionFeedbackDto {
    private String content;

    @JsonProperty("behavefeedback")
    private String behaveFeedback;

    @JsonProperty("langfeedback")
    private String langFeedback;

    @JsonProperty("isTailQ")
    private Boolean isTailQ;

    private String viewableUrl;
}
