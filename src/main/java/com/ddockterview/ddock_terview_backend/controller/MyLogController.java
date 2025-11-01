package com.ddockterview.ddock_terview_backend.controller;

import com.ddockterview.ddock_terview_backend.dto.scoreNfeedback.LogDetailResponseDto;
import com.ddockterview.ddock_terview_backend.dto.scoreNfeedback.MyLogResponseDto;
import com.ddockterview.ddock_terview_backend.service.MyLogService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/mylog")
public class MyLogController {

    private final MyLogService myLogService;

    @GetMapping
    public ResponseEntity<MyLogResponseDto> getMyLog(@AuthenticationPrincipal UserDetails userDetails) {

        String userId = userDetails.getUsername();
        MyLogResponseDto response = myLogService.getMyLog(userId);

        return ResponseEntity.ok(response);
    }

    @GetMapping("/{sessionId}")
    public ResponseEntity<LogDetailResponseDto> getLogDetail(
            @PathVariable Long sessionId,
            @AuthenticationPrincipal UserDetails userDetails) {

        String userId = userDetails.getUsername();
        LogDetailResponseDto response = myLogService.getLogDetail(sessionId, userId);

        return ResponseEntity.ok(response);

    }

}
