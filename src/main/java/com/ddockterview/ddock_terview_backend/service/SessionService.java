package com.ddockterview.ddock_terview_backend.service;

import com.ddockterview.ddock_terview_backend.dto.s3.UploadCompleteRequestDto;
import com.ddockterview.ddock_terview_backend.dto.session.SessionRequestDto;
import com.ddockterview.ddock_terview_backend.dto.session.SessionResponseDto;
import com.ddockterview.ddock_terview_backend.entity.Session;
import com.ddockterview.ddock_terview_backend.entity.User;
import com.ddockterview.ddock_terview_backend.entity.enums.InterviewerType;
import com.ddockterview.ddock_terview_backend.repository.SessionRepository;
import com.ddockterview.ddock_terview_backend.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.NoSuchElementException;
import java.util.Random;

@Service
@RequiredArgsConstructor
@Transactional
public class SessionService {

    private static final Logger log = LoggerFactory.getLogger(SessionService.class);
    private final SessionRepository sessionRepository;
    private final UserRepository userRepository;
    private final S3LinkService s3LinkService; // S3LinkService 주입
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

    /**
     * S3 업로드 완료 후 영상 분석을 처리하는 메서드
     * @param user 현재 로그인한 사용자
     * @param requestDto s3Key, sessionId, questionId 정보
     */
    public void processVideoAnalysis(User user, UploadCompleteRequestDto requestDto) {
        // 1. 요청 유효성 검증 (해당 세션이 사용자의 것이 맞는지 등)
        Session session = sessionRepository.findById(requestDto.getSessionId())
                .orElseThrow(() -> new NoSuchElementException("해당 세션을 찾을 수 없습니다. id=" + requestDto.getSessionId()));

        if (!session.getSessionUser().getUserId().equals(user.getUserId())) {
            throw new SecurityException("세션에 접근할 권한이 없습니다.");
        }

        // 2. 분석을 위한 다운로드 URL 발급
        String downloadUrl = s3LinkService.getDownloadUrl(requestDto.getS3Key());
        log.info("S3 Download URL for analysis: {}", downloadUrl);

        // 3. (향후 구현) Google Cloud Gemini API로 분석 요청
        // log.info("Sending video to Google Cloud AI for analysis...");
        // geminiApiClient.analyzeVideo(downloadUrl, requestDto.getQuestionId());

        // 4. (향후 구현) 분석 결과 DB에 저장
        // - QuestionAfter 테이블에 s3Key, videoUrl(downloadUrl) 등 저장
        // - ScoreAndFeedback 테이블에 분석 결과 저장
        log.info("Video analysis process for questionId {} in session {} is triggered.", requestDto.getQuestionId(), requestDto.getSessionId());

    }
}
