package com.ddockterview.ddock_terview_backend.service;

import com.ddockterview.ddock_terview_backend.dto.scoreNfeedback.GrowthReportDto;
import com.ddockterview.ddock_terview_backend.dto.scoreNfeedback.MyLogResponseDto;
import com.ddockterview.ddock_terview_backend.dto.scoreNfeedback.SessionLogDto;
import com.ddockterview.ddock_terview_backend.entity.ScoreAndFeedback;
import com.ddockterview.ddock_terview_backend.entity.Session;
import com.ddockterview.ddock_terview_backend.entity.User;
import com.ddockterview.ddock_terview_backend.repository.ScoreAndFeedbackRepository;
import com.ddockterview.ddock_terview_backend.repository.SessionRepository;
import com.ddockterview.ddock_terview_backend.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional
public class MyLogService {

    private final UserRepository userRepository;
    private final SessionRepository sessionRepository;
    private final ScoreAndFeedbackRepository scoreAndFeedbackRepository;

    public MyLogResponseDto getMyLog(String userId){
        User user = userRepository.findByUserId(userId)
                .orElseThrow(() -> new IllegalArgumentException("사용자를 찾을 수 없습니다: " + userId));

        List<Session> ascendingSessions = sessionRepository.findAllBySessionUserOrderByCreatedAtAsc(user);

        // 3. (그래프용) GrowthReport DTO 생성
        List<String> labels = new ArrayList<>();
        List<Integer> scores = new ArrayList<>();

        // 세션 목록과 피드백을 임시로 매핑
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

}
