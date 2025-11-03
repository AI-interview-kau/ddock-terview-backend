package com.ddockterview.ddock_terview_backend.service;

import com.ddockterview.ddock_terview_backend.dto.note.NoteResponseDto;
import com.ddockterview.ddock_terview_backend.entity.BaseQuestion;
import com.ddockterview.ddock_terview_backend.entity.Note;
import com.ddockterview.ddock_terview_backend.entity.QuestionAfter;
import com.ddockterview.ddock_terview_backend.entity.User;
import com.ddockterview.ddock_terview_backend.repository.BaseQuestionRepository;
import com.ddockterview.ddock_terview_backend.repository.NoteRepository;
import com.ddockterview.ddock_terview_backend.repository.QuestionAfterRepository;
import com.ddockterview.ddock_terview_backend.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional
public class NoteService {

    private final NoteRepository noteRepository;
    private final UserRepository userRepository;
    private final BaseQuestionRepository baseQuestionRepository;
    private final QuestionAfterRepository questionAfterRepository;

    public NoteResponseDto getNoteByQuestionId(String prefixedQuestionId, String authenticatedUserId) {

        // 1. "B:12" 형식의 ID 파싱
        String[] parts = prefixedQuestionId.split(":");
        if (parts.length != 2) {
            throw new IllegalArgumentException("잘못된 questionId 형식입니다.");
        }

        String type = parts[0];
        Long id = Long.parseLong(parts[1]);

        Note note;

        // 2. 타입에 따라 분기
        if ("B".equals(type)) {
            // [B타입: 1-N 관계]
            // 1. 인증된 사용자부터 찾음
            User user = userRepository.findByUserId(authenticatedUserId)
                    .orElseThrow(() -> new IllegalArgumentException("사용자를 찾을 수 없습니다."));
            // 2. 질문 원본을 찾음
            BaseQuestion bq = baseQuestionRepository.findById(id)
                    .orElseThrow(() -> new IllegalArgumentException("해당 노트를 찾을 수 없습니다 : " + prefixedQuestionId));

            // 3. (유저 + 질문)으로 노트를 찾음
            note = noteRepository.findByUserAndBaseQuestion(user, bq)
                    .orElseThrow(() -> new IllegalArgumentException("해당 노트를 찾을 수 없습니다 : " + prefixedQuestionId));

        } else if ("A".equals(type)) {
            // [A타입: 1-1 관계]
            // 1. 질문 원본(QuestionAfter)을 찾음
            QuestionAfter qa = questionAfterRepository.findById(id)
                    .orElseThrow(() -> new IllegalArgumentException("해당 노트를 찾을 수 없습니다 : " + prefixedQuestionId));

            // 2. 질문에 1:1로 연결된 노트를 찾음
            note = noteRepository.findByQuestionAfterIs(qa)
                    .orElseThrow(() -> new IllegalArgumentException("해당 노트를 찾을 수 없습니다 : " + prefixedQuestionId));

            // 3. [보안] 찾은 노트의 소유자가 로그인한 사용자인지 확인
            if (!note.getUser().getUserId().equals(authenticatedUserId)) {
                throw new AccessDeniedException("이 노트에 접근할 권한이 없습니다.");
            }

        } else {
            throw new IllegalArgumentException("알 수 없는 questionId 타입입니다.");
        }

        // 4. DTO로 변환하여 반환
        return new NoteResponseDto(note);
    }

}
