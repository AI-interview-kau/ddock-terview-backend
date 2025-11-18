package com.ddockterview.ddock_terview_backend.controller;

import com.ddockterview.ddock_terview_backend.dto.GlobalResponseDto;
import com.ddockterview.ddock_terview_backend.dto.feedback.FeedbackRequestDto;
import com.ddockterview.ddock_terview_backend.service.FeedbackService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api")
public class FeedbackController {

    private final FeedbackService feedbackService;

    @PostMapping("/feedback")
    public ResponseEntity<GlobalResponseDto> saveFeedback(@RequestBody FeedbackRequestDto requestDto) {
        System.out.println(">>> /api/feedback called");

        feedbackService.saveAllFeedback(requestDto);
        GlobalResponseDto response = new GlobalResponseDto(200, "모든 피드백을 성공적으로 저장했습니다.");
        return ResponseEntity.ok(response);
    }
}
