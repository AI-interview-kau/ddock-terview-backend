package com.ddockterview.ddock_terview_backend.controller;

import com.ddockterview.ddock_terview_backend.dto.GlobalResponseDto;
import com.ddockterview.ddock_terview_backend.dto.qlist.MyQuestionRequestDto;
import com.ddockterview.ddock_terview_backend.dto.qlist.MyQuestionResponseDto;
import com.ddockterview.ddock_terview_backend.dto.qlist.SaveQuestionRequestDto;
import com.ddockterview.ddock_terview_backend.dto.qlist.SaveQuestionResponseDto;
import com.ddockterview.ddock_terview_backend.entity.User;
import com.ddockterview.ddock_terview_backend.service.QuestionListService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;


@RestController
@RequiredArgsConstructor
public class QuestionListController {

    private final QuestionListService questionListService;

    // 내가 만든 질문 저장
    @PostMapping("/questions")
    public ResponseEntity<MyQuestionResponseDto> createMyQuestion(@AuthenticationPrincipal UserDetails userDetails,
                                                                  @RequestBody MyQuestionRequestDto requestDto) {
        MyQuestionResponseDto response = questionListService.createMyQuestion(userDetails.getUsername(), requestDto);

        return ResponseEntity.ok(response);
    }

    // 내가 만든 질문 삭제
    @DeleteMapping("/questions/{bqId}")
    public ResponseEntity<GlobalResponseDto> deleteMyQuestion(@AuthenticationPrincipal UserDetails userDetails,
                                                 @PathVariable Long bqId) {
        questionListService.deleteMyQuestion(userDetails.getUsername(), bqId);
        GlobalResponseDto response = new GlobalResponseDto(200, "질문이 성공적으로 삭제되었습니다.");
        return ResponseEntity.ok(response);
    }

    // 질문 찜하기 (저장)
    @PostMapping("/savedQ")
    public ResponseEntity<SaveQuestionResponseDto> saveQuestion(@AuthenticationPrincipal UserDetails userDetails,
                                                                @RequestBody SaveQuestionRequestDto requestDto) {
        SaveQuestionResponseDto response = questionListService.saveQuestion(userDetails.getUsername(), requestDto);
        return ResponseEntity.ok(response);
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
