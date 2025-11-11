package com.ddockterview.ddock_terview_backend.controller;

import com.ddockterview.ddock_terview_backend.dto.GlobalResponseDto;
import com.ddockterview.ddock_terview_backend.dto.savedQ.SaveQuestionListDto;
import com.ddockterview.ddock_terview_backend.dto.savedQ.SaveQuestionRequestDto;
import com.ddockterview.ddock_terview_backend.dto.savedQ.SaveQuestionResponseDto;
import com.ddockterview.ddock_terview_backend.service.QuestionListService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
public class SavedQController {

    private final QuestionListService questionListService;

    // 질문 찜하기 (저장)
    @PostMapping("/savedQ")
    public ResponseEntity<SaveQuestionResponseDto> saveQuestion(@AuthenticationPrincipal UserDetails userDetails,
                                                                @RequestBody SaveQuestionRequestDto requestDto) {
        SaveQuestionResponseDto response = questionListService.saveQuestion(userDetails.getUsername(), requestDto);
        return ResponseEntity.ok(response);
    }

    // 질문 찜하기 취소 (삭제)
    @DeleteMapping("/savedQ")
    public ResponseEntity<GlobalResponseDto> deleteSavedQuestion(@AuthenticationPrincipal UserDetails userDetails,
                                                                 @RequestBody SaveQuestionRequestDto requestDto) {
        questionListService.deleteSavedQuestion(userDetails.getUsername(), requestDto.getBqId(), requestDto.getInqId());

        GlobalResponseDto response = new GlobalResponseDto(200, "질문이 성공적으로 삭제되었습니다.");
        return ResponseEntity.ok(response);
    }

    // 찜한 질문 목록 조회
    @GetMapping("/savedQ")
    public ResponseEntity<SaveQuestionListDto> getSavedQuestions(@AuthenticationPrincipal UserDetails userDetails) {
        SaveQuestionListDto response = questionListService.getSavedQuestions(userDetails.getUsername());
        return ResponseEntity.ok(response);
    }

}
