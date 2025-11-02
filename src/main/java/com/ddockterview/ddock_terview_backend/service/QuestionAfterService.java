package com.ddockterview.ddock_terview_backend.service;

import com.ddockterview.ddock_terview_backend.dto.question.QuestionSaveRequestDto;
import com.ddockterview.ddock_terview_backend.dto.question.QuestionSaveResponseDto;
import com.ddockterview.ddock_terview_backend.entity.QuestionAfter;
import com.ddockterview.ddock_terview_backend.entity.Session;
import com.ddockterview.ddock_terview_backend.repository.QuestionAfterRepository;
import com.ddockterview.ddock_terview_backend.repository.SessionRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.stereotype.Service;

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

}
