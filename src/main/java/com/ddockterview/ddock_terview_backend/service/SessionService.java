package com.ddockterview.ddock_terview_backend.service;

import com.ddockterview.ddock_terview_backend.dto.session.SessionRequestDto;
import com.ddockterview.ddock_terview_backend.dto.session.SessionResponseDto;
import com.ddockterview.ddock_terview_backend.entity.Session;
import com.ddockterview.ddock_terview_backend.entity.User;
import com.ddockterview.ddock_terview_backend.entity.enums.InterviewerType;
import com.ddockterview.ddock_terview_backend.repository.SessionRepository;
import com.ddockterview.ddock_terview_backend.repository.UserRepository;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.apache.catalina.security.SecurityUtil;
import org.springframework.security.access.AccessDeniedException;
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

    public void deleteSession(Long sessionId, String userId) {

        User user = userRepository.findByUserId(userId)
                .orElseThrow(() -> new IllegalArgumentException("해당 사용자를 찾을 수 없습니다: " + userId));

        Session session = sessionRepository.findById(sessionId)
                .orElseThrow(() -> new EntityNotFoundException("해당 세션을 찾을 수 없습니다. id=" + sessionId)); // 404 Not Found

        // 권한을 확인합니다. (세션의 소유자와 현재 로그인한 사용자가 같은지)
        if (session.getSessionUser() == null || !session.getSessionUser().getUserId().equals(user.getUserId())) {
            throw new AccessDeniedException("이 세션을 삭제할 권한이 없습니다."); // 403 Forbidden
        }

        // 4. 세션을 삭제합니다.
        //    (@OneToMany에 설정한 cascade 옵션 덕분에
        //     연관된 QuestionAfter, ScoreAndFeedback 등도 모두 함께 삭제됩니다.)
        sessionRepository.delete(session);
    }

    private InterviewerType getRandomInterviewerType() {
        InterviewerType[] types = InterviewerType.values();
        return types[RAND.nextInt(types.length)];
    }



}
