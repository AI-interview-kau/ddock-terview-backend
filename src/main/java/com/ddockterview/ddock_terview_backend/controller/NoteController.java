package com.ddockterview.ddock_terview_backend.controller;

import com.ddockterview.ddock_terview_backend.dto.note.NoteResponseDto;
import com.ddockterview.ddock_terview_backend.dto.note.NoteSaveRequestDto;
import com.ddockterview.ddock_terview_backend.service.NoteService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/note")
public class NoteController {

    private final NoteService noteService;

    @GetMapping("/{questionId}")
    public ResponseEntity<NoteResponseDto> getNote(
            @PathVariable String questionId,
            @AuthenticationPrincipal UserDetails userDetails) {

        String userId = userDetails.getUsername();
        NoteResponseDto response = noteService.getNoteByQuestionId(questionId, userId);

        return ResponseEntity.ok(response);
    }

    @PutMapping("/{questionId}")
    public ResponseEntity<NoteResponseDto> saveOrUpdateNote(
            @PathVariable String questionId,
            @RequestBody NoteSaveRequestDto requestDto,
            @AuthenticationPrincipal UserDetails userDetails
            ){
        String userId = userDetails.getUsername();
        NoteService.UpsertResult<NoteResponseDto> result = noteService.upsertNote(questionId, userId, requestDto);

        if (result.isCreated()) {
            // 201 Created: 새로 생성됨
            return ResponseEntity.status(HttpStatus.CREATED).body(result.getDto());
        } else {
            // 200 OK: 기존 내용 수정
            return ResponseEntity.ok(result.getDto());
        }
    }

}
