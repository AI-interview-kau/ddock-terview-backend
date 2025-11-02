package com.ddockterview.ddock_terview_backend.controller;

import com.ddockterview.ddock_terview_backend.dto.question.QuestionSaveRequestDto;
import com.ddockterview.ddock_terview_backend.dto.question.QuestionSaveResponseDto;
import com.ddockterview.ddock_terview_backend.service.QuestionAfterService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class QuestionAfterController {

    private final QuestionAfterService questionAfterService;

    @PostMapping("/session/{sessionId}/questions")
    public ResponseEntity<QuestionSaveResponseDto> saveQuestions(
            @PathVariable Long sessionId,
            @RequestBody QuestionSaveRequestDto requestDto,
            @AuthenticationPrincipal UserDetails userDetails
    ){
        String userId = userDetails.getUsername();

        QuestionSaveResponseDto responseDto = questionAfterService.saveQuestions(sessionId, requestDto, userId);

        return ResponseEntity.ok(responseDto);
    }

}
