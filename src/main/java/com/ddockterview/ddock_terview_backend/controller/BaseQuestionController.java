package com.ddockterview.ddock_terview_backend.controller;

import com.ddockterview.ddock_terview_backend.dto.GlobalResponseDto;
import com.ddockterview.ddock_terview_backend.dto.qlist.MyQuestionRequestDto;
import com.ddockterview.ddock_terview_backend.dto.qlist.MyQuestionResponseDto;
import com.ddockterview.ddock_terview_backend.dto.qlist.QuestionListResponseDto;
import com.ddockterview.ddock_terview_backend.service.BaseQuestionService;
import com.ddockterview.ddock_terview_backend.service.QuestionListService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping
public class BaseQuestionController {

    private final BaseQuestionService questionService;
    private final QuestionListService questionListService;

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

}
