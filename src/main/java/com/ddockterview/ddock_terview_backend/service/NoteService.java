package com.ddockterview.ddock_terview_backend.service;

import com.ddockterview.ddock_terview_backend.dto.note.NoteResponseDto;
import com.ddockterview.ddock_terview_backend.dto.note.NoteSaveRequestDto;
import com.ddockterview.ddock_terview_backend.entity.BaseQuestion;
import com.ddockterview.ddock_terview_backend.entity.Note;
import com.ddockterview.ddock_terview_backend.entity.QuestionAfter;
import com.ddockterview.ddock_terview_backend.entity.User;
import com.ddockterview.ddock_terview_backend.repository.BaseQuestionRepository;
import com.ddockterview.ddock_terview_backend.repository.NoteRepository;
import com.ddockterview.ddock_terview_backend.repository.QuestionAfterRepository;
import com.ddockterview.ddock_terview_backend.repository.UserRepository;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

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



    @Transactional
    public UpsertResult<NoteResponseDto> upsertNote(String prefixedQuestionId, String authenticatedUserId, NoteSaveRequestDto requestDto) {

        // 1. 사용자 조회
        User user = userRepository.findByUserId(authenticatedUserId)
                .orElseThrow(() -> new IllegalArgumentException("사용자를 찾을 수 없습니다."));

        // 2. ID 파싱 (B:12 -> BaseQuestion / A:101 -> QuestionAfter)
        Object[] questionAndType = parseQuestionId(prefixedQuestionId);
        String type = (String) questionAndType[0];
        Object questionEntity = questionAndType[1];

        Optional<Note> existingNote;
        BaseQuestion bq = null;
        QuestionAfter qa = null;

        // 3. 타입에 따라 기존 노트 조회
        if ("B".equals(type)) {
            bq = (BaseQuestion) questionEntity;
            existingNote = noteRepository.findByUserAndBaseQuestion(user, bq);
        } else if ("A".equals(type)) {
            qa = (QuestionAfter) questionEntity;
            existingNote = noteRepository.findByQuestionAfterIs(qa);
        } else {
            throw new IllegalArgumentException("알 수 없는 questionId 타입입니다.");
        }

        Note note;
        boolean isCreated; // 201(true)인지 200(false)인지 구분하기 위한 플래그

        if (existingNote.isPresent()) {
            // 4-1. [수정] 노트가 이미 존재하면 (200 OK)
            note = existingNote.get();

            // [보안] QuestionAfter(A타입)의 경우, 찾은 노트의 소유권 재확인
            if ("A".equals(type) && !note.getUser().getUserId().equals(authenticatedUserId)) {
                throw new AccessDeniedException("이 노트에 접근할 권한이 없습니다.");
            }

            note.setContent(requestDto.getContent()); // 내용 업데이트
            isCreated = false; // 수정됨
        } else {
            // 4-2. [생성] 노트가 없으면 (201 Created)
            // (A타입의 경우, QuestionAfter의 소유권 확인 로직이 필요하다면 여기에 추가)
            if ("A".equals(type) && !qa.getSession().getSessionUser().getUserId().equals(authenticatedUserId)) {
                throw new AccessDeniedException("이 질문에 노트를 생성할 권한이 없습니다.");
            }

            note = Note.builder()
                    .user(user)
                    .baseQuestion(bq)       // bq가 null이 아니면(B타입) 설정됨
                    .questionAfter(qa)      // qa가 null이 아니면(A타입) 설정됨
                    .content(requestDto.getContent())
                    .build();

            noteRepository.save(note);
            isCreated = true; // 새로 생성됨
        }

        return new UpsertResult<>(isCreated, new NoteResponseDto(note));
    }


    // ▼▼▼ 중복 로직을 처리할 private 헬퍼 메서드 추가 ▼▼▼
    /**
     * "B:12" 같은 ID를 파싱하여 실제 엔티티 객체와 타입을 반환합니다.
     * @return Object[] {String type ("B" or "A"), Object entity (BaseQuestion or QuestionAfter)}
     */
    private Object[] parseQuestionId(String prefixedQuestionId) {
        String[] parts = prefixedQuestionId.split(":");
        if (parts.length != 2) {
            throw new IllegalArgumentException("잘못된 questionId 형식입니다.");
        }

        String type = parts[0];
        Long id = Long.parseLong(parts[1]);

        if ("B".equals(type)) {
            BaseQuestion bq = baseQuestionRepository.findById(id)
                    .orElseThrow(() -> new IllegalArgumentException("노트를 찾을 수 없습니다 : " + prefixedQuestionId));
            return new Object[]{type, bq};
        } else if ("A".equals(type)) {
            QuestionAfter qa = questionAfterRepository.findById(id)
                    .orElseThrow(() -> new IllegalArgumentException("노트를 찾을 수 없습니다 : " + prefixedQuestionId));
            return new Object[]{type, qa};
        } else {
            throw new IllegalArgumentException("알 수 없는 questionId 타입입니다.");
        }
    }



    @Getter
    @AllArgsConstructor
    public static class UpsertResult<T> {
        private boolean created; // 201이면 true, 200이면 false
        private T dto;
    }

}
