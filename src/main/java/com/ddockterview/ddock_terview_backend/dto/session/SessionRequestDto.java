package com.ddockterview.ddock_terview_backend.dto.session;

import com.ddockterview.ddock_terview_backend.entity.enums.InterviewType;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class SessionRequestDto {
    private InterviewType interviewType;
}
