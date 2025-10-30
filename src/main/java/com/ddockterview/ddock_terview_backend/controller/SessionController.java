package com.ddockterview.ddock_terview_backend.controller;

import com.ddockterview.ddock_terview_backend.dto.session.SessionRequestDto;
import com.ddockterview.ddock_terview_backend.dto.session.SessionResponseDto;
import com.ddockterview.ddock_terview_backend.service.SessionService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/session")
public class SessionController {

    private final SessionService sessionService;

    @PostMapping
    public ResponseEntity<SessionResponseDto> createSession(
            @RequestBody SessionRequestDto requestDto,
            @AuthenticationPrincipal UserDetails userDetails) {

        String userId = userDetails.getUsername();
        SessionResponseDto responseDto = sessionService.createSession(requestDto, userId);
        return ResponseEntity.ok(responseDto);
    }

}
