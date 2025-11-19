package com.ddockterview.ddock_terview_backend.controller;

import com.ddockterview.ddock_terview_backend.dto.GlobalResponseDto;
import com.ddockterview.ddock_terview_backend.service.FeedbackService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api")
public class FeedbackController {

    private final FeedbackService feedbackService;

    @PostMapping(value = "/feedback", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<GlobalResponseDto> saveFeedback(@RequestParam("file") MultipartFile file) throws IOException {
        System.out.println(">>> /api/feedback called with a file");

        feedbackService.saveAllFeedback(file);
        GlobalResponseDto response = new GlobalResponseDto(200, "모든 피드백을 성공적으로 저장했습니다.");
        return ResponseEntity.ok(response);
    }
}
