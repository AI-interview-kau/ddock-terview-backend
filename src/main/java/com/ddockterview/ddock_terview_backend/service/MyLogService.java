package com.ddockterview.ddock_terview_backend.service;

import com.ddockterview.ddock_terview_backend.dto.feedbackPerQ.QuestionFeedbackDetailDto;
import com.ddockterview.ddock_terview_backend.dto.scoreNfeedback.*;
import com.ddockterview.ddock_terview_backend.entity.*;
import com.ddockterview.ddock_terview_backend.repository.*;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional
public class MyLogService {

    private final UserRepository userRepository;
    private final SessionRepository sessionRepository;
    private final ScoreAndFeedbackRepository scoreAndFeedbackRepository;
    private final QuestionAfterRepository questionAfterRepository;
    private final FeedbackPerQRepository feedbackPerQRepository;

    public MyLogResponseDto getMyLog(String userId){
        User user = userRepository.findByUserId(userId)
                .orElseThrow(() -> new IllegalArgumentException("사용자를 찾을 수 없습니다: " + userId));

        List<Session> ascendingSessions = sessionRepository.findAllBySessionUserOrderByCreatedAtAsc(user);

        List<String> labels = new ArrayList<>();
        List<Integer> scores = new ArrayList<>();

        List<SessionLogDto> sessionLogDtosAsc = new ArrayList<>();

        for (int i = 0; i < ascendingSessions.size(); i++) {
            Session session = ascendingSessions.get(i);
            int index = i + 1; // "N차" (1, 2, 3...)

            // 세션에 해당하는 피드백 조회
            ScoreAndFeedback feedback = scoreAndFeedbackRepository.findBySession(session)
                    .orElse(null); // 피드백이 아직 없을 수 있음

            // 그래프 데이터 추가
            labels.add(index + "차");
            scores.add(feedback != null ? feedback.getTotalScore() : 0);

            // 세션 로그 DTO 생성 (아직 오름차순 상태)
            sessionLogDtosAsc.add(new SessionLogDto(session, feedback, index));
        }

        GrowthReportDto growthReport = new GrowthReportDto(labels, scores);

        Collections.reverse(sessionLogDtosAsc);
        List<SessionLogDto> descendingSessionLogs = sessionLogDtosAsc;

        return new MyLogResponseDto(growthReport, descendingSessionLogs);

    }

    public LogDetailResponseDto getLogDetail(Long sessionId, String authenticatedUserId) {

        Session session = sessionRepository.findById(sessionId)
                .orElseThrow(() -> new IllegalArgumentException("세션을 찾을 수 없습니다: " + sessionId));

        if (!session.getSessionUser().getUserId().equals(authenticatedUserId)) {
            throw new AccessDeniedException("이 기록에 접근할 권한이 없습니다.");
        }

        FeedbackDetailDto feedbackDto = scoreAndFeedbackRepository.findBySession(session)
                .map(FeedbackDetailDto::new)
                .orElse(new FeedbackDetailDto());

        List<QuestionAfter> questions = questionAfterRepository.findAllBySession(session);

        List<String> questionContents = questions.stream()
                .map(QuestionAfter::getContent)
                .collect(Collectors.toList());

        return new LogDetailResponseDto(feedbackDto, questionContents);
    }

    public QuestionFeedbackDetailDto getQuestionFeedbackDetail(Long sessionId, Long inqId, String userId){

        QuestionAfter question = questionAfterRepository.findById(inqId)
                .orElseThrow(() -> new IllegalArgumentException("해당 질문을 찾을 수 없습니다: " + inqId));

        Session session = question.getSession();
        if (session == null) {
            throw new IllegalStateException("질문에 연결된 세션이 없습니다.");
        }

        // URL로 받은 sessionId와 실제 질문에 연결된 세션 ID가 일치하는지 확인
        if (!session.getSessionId().equals(sessionId)) {
            throw new AccessDeniedException("요청한 세션 ID와 질문의 세션 ID가 일치하지 않습니다.");
        }

        // 해당 세션의 소유자가 인증된 사용자인지 확인
        if (!session.getSessionUser().getUserId().equals(userId)) {
            throw new AccessDeniedException("이 기록에 접근할 권한이 없습니다.");
        }

        FeedbackPerQ feedback = feedbackPerQRepository.findByQuestionAfterIs(question)
                .orElse(null);

        return new QuestionFeedbackDetailDto(question, feedback);

    }




}
