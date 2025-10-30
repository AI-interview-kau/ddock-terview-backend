package com.ddockterview.ddock_terview_backend.controller;


import com.ddockterview.ddock_terview_backend.dto.login.LoginRequestDto;
import com.ddockterview.ddock_terview_backend.dto.login.JwtToken;
import com.ddockterview.ddock_terview_backend.dto.user.UserResponseDto;
import com.ddockterview.ddock_terview_backend.entity.User;
import com.ddockterview.ddock_terview_backend.service.UserService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/user")
public class UserController {

    private final UserService userService;

    @PostMapping("/login")
    public JwtToken login(@RequestBody LoginRequestDto loginRequestDto) {
        String userId = loginRequestDto.getUserId();
        String password = loginRequestDto.getPassword();
        JwtToken jwtToken = userService.login(userId, password);
        log.info("request username = {}, password = {}", userId, password);
        log.info("jwtToken accessToken = {}, refreshToken = {}", jwtToken.getAccessToken(), jwtToken.getRefreshToken());
        return jwtToken;
    }

    @GetMapping
    public ResponseEntity<UserResponseDto> getUser(@AuthenticationPrincipal UserDetails userDetails) {

        String userId = userDetails.getUsername();

        UserResponseDto userResponseDto = userService.getUser(userId);

        return ResponseEntity.ok(userResponseDto);
    }

}
