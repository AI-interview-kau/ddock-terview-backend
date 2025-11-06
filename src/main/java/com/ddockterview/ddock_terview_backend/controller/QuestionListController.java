package com.ddockterview.ddock_terview_backend.controller;

import com.ddockterview.ddock_terview_backend.dto.qlist.MyQuestionRequestDto;
import com.ddockterview.ddock_terview_backend.dto.qlist.MyQuestionResponseDto;
import com.ddockterview.ddock_terview_backend.dto.qlist.SaveQuestionRequestDto;
import com.ddockterview.ddock_terview_backend.entity.User;
import com.ddockterview.ddock_terview_backend.service.QuestionListService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

import jakarta.validation.Valid;
import java.net.URI;

@RestController
@RequiredArgsConstructor
@RequestMapping("/questions")
public class QuestionListController {

    private final QuestionListService questionListService;

    // 내가 만든 질문 저장
    @PostMapping
    public ResponseEntity<MyQuestionResponseDto> createMyQuestion(@AuthenticationPrincipal UserDetails userDetails,
                                                                  @RequestBody MyQuestionRequestDto requestDto) {
        MyQuestionResponseDto response = questionListService.createMyQuestion(userDetails.getUsername(), requestDto);

        return ResponseEntity.ok(response);
    }

    // 내가 만든 질문 삭제
    @DeleteMapping("/my/{questionId}")
    public ResponseEntity<Void> deleteMyQuestion(@AuthenticationPrincipal User user,
                                                 @PathVariable Long questionId) {
        questionListService.deleteMyQuestion(user, questionId);
        return ResponseEntity.noContent().build();
    }

    // 질문 찜하기 (저장)
    @PostMapping("/saved")
    public ResponseEntity<Void> saveQuestion(@AuthenticationPrincipal User user,
                                             @Valid @RequestBody SaveQuestionRequestDto requestDto) {
        questionListService.saveQuestion(user, requestDto);
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }

    // 질문 찜하기 취소 (삭제)
    @DeleteMapping("/saved")
    public ResponseEntity<Void> deleteSavedQuestion(@AuthenticationPrincipal User user,
                                                    @RequestParam(value = "bq_id", required = false) Long baseQuestionId,
                                                    @RequestParam(value = "inq_id", required = false) Long interviewQuestionId) {
        questionListService.deleteSavedQuestion(user, baseQuestionId, interviewQuestionId);
        return ResponseEntity.noContent().build();
    }
}
