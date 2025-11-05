package com.ddockterview.ddock_terview_backend.service;

import com.ddockterview.ddock_terview_backend.dto.question.AnswerUpdateRequestDto;
import com.ddockterview.ddock_terview_backend.dto.question.QuestionAfterResponseDto;
import com.ddockterview.ddock_terview_backend.dto.question.QuestionSaveRequestDto;
import com.ddockterview.ddock_terview_backend.dto.question.QuestionSaveResponseDto;
import com.ddockterview.ddock_terview_backend.entity.QuestionAfter;
import com.ddockterview.ddock_terview_backend.entity.Session;
import com.ddockterview.ddock_terview_backend.repository.QuestionAfterRepository;
import com.ddockterview.ddock_terview_backend.repository.SessionRepository;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class QuestionAfterService {

    private final SessionRepository sessionRepository;
    private final QuestionAfterRepository questionAfterRepository;

    public QuestionSaveResponseDto saveQuestions(Long sessionId, QuestionSaveRequestDto requestDto, String userId) {

        Session session = sessionRepository.findById(sessionId)
                .orElseThrow(()-> new IllegalArgumentException("Session not found: " + sessionId));

        if(!session.getSessionUser().getUserId().equals(userId)) {
            throw new AccessDeniedException("Access denied");
        }

        List<QuestionAfter> questionsToSave = requestDto.getItems().stream()
                .map(questionContentDto -> QuestionAfter.builder()
                        .session(session)
                        .content(questionContentDto.getContent())
                        .isTailQ(false)
                        .build())
                .collect(Collectors.toList());

        List<QuestionAfter> savedQuestions = questionAfterRepository.saveAll(questionsToSave);

        return new QuestionSaveResponseDto(session, savedQuestions);
    }

    @Transactional
    public QuestionAfterResponseDto updateAnswer(Long inqId, AnswerUpdateRequestDto requestDto) {
        QuestionAfter questionAfter = questionAfterRepository.findById(inqId)
                .orElseThrow(() -> new EntityNotFoundException("해당 질문을 찾을 수 없습니다. id=" + inqId));

        questionAfter.updateAnswer(requestDto.getAnswer());

        // @Transactional에 의해 메서드가 커밋될 때 변경 사항이 DB에 자동 반영(UPDATE 쿼리)됩니다.
        // (questionAfterRepository.save()를 호출할 필요가 없습니다.)

        return new QuestionAfterResponseDto(questionAfter);
    }

}
