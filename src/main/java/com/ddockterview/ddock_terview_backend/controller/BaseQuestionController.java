package com.ddockterview.ddock_terview_backend.controller;

import com.ddockterview.ddock_terview_backend.dto.qlist.QuestionListResponseDto;
import com.ddockterview.ddock_terview_backend.service.BaseQuestionService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping
public class BaseQuestionController {

    private final BaseQuestionService questionService;

    @GetMapping("/questionlist")
    public ResponseEntity<QuestionListResponseDto> getQuestions(
            @AuthenticationPrincipal UserDetails userDetails) {

        // 1. 인증된 사용자 정보에서 userId(username) 추출
        String userId = userDetails.getUsername();

        // 2. 서비스 호출
        QuestionListResponseDto response = questionService.getQuestionList(userId);

        // 3. 200 OK 응답 반환
        return ResponseEntity.ok(response);
    }

}
