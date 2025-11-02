package com.ddockterview.ddock_terview_backend.controller;

import com.ddockterview.ddock_terview_backend.dto.s3.PresignedUrlResponseDto;
import com.ddockterview.ddock_terview_backend.dto.s3.UploadCompleteRequestDto;
import com.ddockterview.ddock_terview_backend.entity.User;
import com.ddockterview.ddock_terview_backend.service.S3LinkService;
import com.ddockterview.ddock_terview_backend.service.SessionService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;
import java.util.UUID;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/s3")
public class S3Controller {

    private final S3LinkService s3LinkService;
    private final SessionService sessionService;

    @PostMapping("/presigned-url/upload")
    public ResponseEntity<PresignedUrlResponseDto> getUploadPresignedUrl(@AuthenticationPrincipal User user) {
        String s3Key = "videos/" + user.getUserId() + "/" + UUID.randomUUID() + ".webm";
        String uploadUrl = s3LinkService.getUploadUrl(s3Key);
        PresignedUrlResponseDto responseDto = new PresignedUrlResponseDto(s3Key, uploadUrl);
        return ResponseEntity.ok(responseDto);
    }

    @PostMapping("/upload-complete")
    public ResponseEntity<Void> handleUploadComplete(@AuthenticationPrincipal User user,
                                                     @Valid @RequestBody UploadCompleteRequestDto requestDto) {
        sessionService.processVideoAnalysis(user, requestDto);
        return ResponseEntity.ok().build();
    }
}
