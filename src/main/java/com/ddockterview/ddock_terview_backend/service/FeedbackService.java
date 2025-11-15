package com.ddockterview.ddock_terview_backend.service;

import com.ddockterview.ddock_terview_backend.dto.feedback.FeedbackRequestDto;
import com.ddockterview.ddock_terview_backend.dto.feedback.QuestionFeedbackDto;
import com.ddockterview.ddock_terview_backend.entity.*;
import com.ddockterview.ddock_terview_backend.repository.*;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional
public class FeedbackService {

    private final SessionRepository sessionRepository;
    private final UserRepository userRepository;
    private final QuestionAfterRepository questionAfterRepository;
    private final ScoreAndFeedbackRepository scoreAndFeedbackRepository;
    private final FeedbackPerQRepository feedbackPerQRepository;

    public void saveAllFeedback(FeedbackRequestDto requestDto) {
        // 1. User 찾기
        User user = userRepository.findByUserId(requestDto.getUserId())
                .orElseThrow(() -> new IllegalArgumentException("사용자를 찾을 수 없습니다: " + requestDto.getUserId()));

        // 2. 해당 유저의 가장 최근 세션 찾기
        Session session = sessionRepository.findFirstBySessionUserOrderByCreatedAtDesc(user)
                .orElseThrow(() -> new IllegalArgumentException("해당 사용자의 세션을 찾을 수 없습니다."));

        // 3. ScoreAndFeedback 저장
        ScoreAndFeedback scoreAndFeedback = ScoreAndFeedback.builder()
                .session(session)
                .totalScore(requestDto.getTotalScore())
                .suitability(requestDto.getSuitability())
                .intendUnderstanding(requestDto.getIntendunderstanding())
                .problemSolving(requestDto.getProblemsolving())
                .accuracy(requestDto.getAccuracy())
                .experience(requestDto.getExperience())
                .logicality(requestDto.getLogicality())
                .confidence(requestDto.getConfidence())
                .speed(requestDto.getSpeed())
                .voice(requestDto.getVoice())
                .gesture(requestDto.getGesture())
                .attitude(requestDto.getAttitude())
                .gazing(requestDto.getGazing())
                .generalFeedback(requestDto.getGeneralFeedback())
                .pro(requestDto.getPro())
                .con(requestDto.getCon())
                .build();
        scoreAndFeedbackRepository.save(scoreAndFeedback);

        // 4. QuestionAfter 및 FeedbackPerQ 저장
        for (QuestionFeedbackDto qDto : requestDto.getQuestions()) {
            QuestionAfter questionAfter = QuestionAfter.builder()
                    .session(session)
                    .content(qDto.getContent())
                    .isTailQ(qDto.getIsTailQ())
                    .videoUrl(qDto.getViewableUrl())
                    .build();
            questionAfterRepository.save(questionAfter);

            FeedbackPerQ feedbackPerQ = FeedbackPerQ.builder()
                    .questionAfter(questionAfter)
                    .behavefeedback(qDto.getBehaveFeedback())
                    .langfeedback(qDto.getLangFeedback())
                    .build();
            feedbackPerQRepository.save(feedbackPerQ);
        }
    }
}
