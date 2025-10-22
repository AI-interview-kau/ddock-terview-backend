package com.ddockterview.ddock_terview_backend.controller;


import com.ddockterview.ddock_terview_backend.dto.login.LoginRequestDto;
import com.ddockterview.ddock_terview_backend.dto.login.JwtToken;
import com.ddockterview.ddock_terview_backend.service.UserService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

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

}
