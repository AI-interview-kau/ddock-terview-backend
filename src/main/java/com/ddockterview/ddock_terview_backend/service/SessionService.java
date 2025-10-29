package com.ddockterview.ddock_terview_backend.service;

import com.ddockterview.ddock_terview_backend.dto.session.SessionRequestDto;
import com.ddockterview.ddock_terview_backend.dto.session.SessionResponseDto;
import com.ddockterview.ddock_terview_backend.entity.Session;
import com.ddockterview.ddock_terview_backend.entity.User;
import com.ddockterview.ddock_terview_backend.entity.enums.InterviewerType;
import com.ddockterview.ddock_terview_backend.repository.SessionRepository;
import com.ddockterview.ddock_terview_backend.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Random;

@Service
@RequiredArgsConstructor
@Transactional
public class SessionService {

    private final SessionRepository sessionRepository;
    private final UserRepository userRepository;
    private static final Random RAND = new Random();

    public SessionResponseDto createSession(SessionRequestDto requestDto, String userId) {

        User user = userRepository.findByUserId(userId)
                .orElseThrow(() -> new IllegalArgumentException("해당 사용자를 찾을 수 없습니다: " + userId));

        InterviewerType randomInterviewerType = getRandomInterviewerType();

        Session newSession = Session.builder()
                .sessionUser(user)
                .interviewType(requestDto.getInterviewType())
                .interviewerType(randomInterviewerType)
                .build();

        Session savedSession = sessionRepository.save(newSession);

        return new SessionResponseDto(savedSession);

    }

    private InterviewerType getRandomInterviewerType() {
        InterviewerType[] types = InterviewerType.values();
        return types[RAND.nextInt(types.length)];
    }

}
