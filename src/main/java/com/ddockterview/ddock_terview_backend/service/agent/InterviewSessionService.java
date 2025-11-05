package com.ddockterview.ddock_terview_backend.service.agent;

import com.ddockterview.ddock_terview_backend.dto.redis.InterviewSession;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import java.time.Duration;
import java.util.List;
import java.util.UUID;

@Service
public class InterviewSessionService {

    private final RedisTemplate<String, Object> redisTemplate;
    private final String KEY_PREFIX = "interview:session:";
    private final Duration SESSION_TIMEOUT = Duration.ofHours(2);

    public InterviewSessionService(RedisTemplate<String, Object> redisTemplate) {
        this.redisTemplate = redisTemplate;
    }

    private String getKey(String sessionId) { return KEY_PREFIX + sessionId; }

    public InterviewSession createSession(Long userId, String resumeS3Url, List<String> mainQuestions) {
        String sessionId = UUID.randomUUID().toString();
        InterviewSession session = new InterviewSession(sessionId, userId, resumeS3Url, mainQuestions);
        redisTemplate.opsForValue().set(getKey(sessionId), session, SESSION_TIMEOUT);
        return session;
    }

    public InterviewSession getSession(String sessionId) {
        return (InterviewSession) redisTemplate.opsForValue().get(getKey(sessionId));
    }

    public void updateSession(InterviewSession session) {
        redisTemplate.opsForValue().set(getKey(session.getSessionId()), session, SESSION_TIMEOUT);
    }

    public void deleteSession(String sessionId) {
        redisTemplate.delete(getKey(sessionId));
    }

}
