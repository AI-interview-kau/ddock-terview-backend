package com.ddockterview.ddock_terview_backend.service;

import com.ddockterview.ddock_terview_backend.dto.qlist.MyQuestionRequestDto;
import com.ddockterview.ddock_terview_backend.dto.qlist.MyQuestionResponseDto;
import com.ddockterview.ddock_terview_backend.dto.qlist.SaveQuestionRequestDto;
import com.ddockterview.ddock_terview_backend.entity.BaseQuestion;
import com.ddockterview.ddock_terview_backend.entity.SavedQuestion;
import com.ddockterview.ddock_terview_backend.entity.User;
import com.ddockterview.ddock_terview_backend.entity.enums.Origin;
import com.ddockterview.ddock_terview_backend.repository.BaseQuestionRepository;
import com.ddockterview.ddock_terview_backend.repository.SavedQuestionRepository;
import com.ddockterview.ddock_terview_backend.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.NoSuchElementException;

@Service
@RequiredArgsConstructor
@Transactional
public class QuestionListService {

    private final BaseQuestionRepository baseQuestionRepository;
    private final SavedQuestionRepository savedQuestionRepository;
    private final UserRepository userRepository;

    // 내가 만든 질문 저장
    public MyQuestionResponseDto createMyQuestion(String userId, MyQuestionRequestDto requestDto) {
        User user = userRepository.findByUserId(userId)
                .orElseThrow(() -> new IllegalArgumentException("사용자를 찾을 수 없습니다."));

        BaseQuestion newQuestion = requestDto.toEntity(user);
        baseQuestionRepository.save(newQuestion);

        return new MyQuestionResponseDto(newQuestion);
    }

    // 내가 만든 질문 삭제
    public void deleteMyQuestion(User user, Long questionId) {
        BaseQuestion question = baseQuestionRepository.findById(questionId)
                .orElseThrow(() -> new NoSuchElementException("해당 질문을 찾을 수 없습니다. id=" + questionId));

        if (question.getUser() == null || !question.getUser().getUserId().equals(user.getUserId())) {
            throw new SecurityException("질문을 삭제할 권한이 없습니다.");
        }
        if (question.getOrigin() != Origin.USER) {
            throw new IllegalArgumentException("사용자가 만든 질문만 삭제할 수 있습니다.");
        }

        // 이 질문을 찜한 모든 기록을 삭제
        savedQuestionRepository.deleteByUserAndBaseQuestion_BqId(user, questionId);
        baseQuestionRepository.delete(question);
    }

    // 질문 찜하기 (저장)
    public Long saveQuestion(User user, SaveQuestionRequestDto requestDto) {
        if (requestDto.getBaseQuestionId() != null) {
            return saveBaseQuestion(user, requestDto.getBaseQuestionId());
        } else if (requestDto.getInterviewQuestionId() != null) {
            return saveInterviewQuestion(user, requestDto.getInterviewQuestionId());
        } else {
            throw new IllegalArgumentException("찜할 질문의 ID를 제공해야 합니다.");
        }
    }

    private Long saveBaseQuestion(User user, Long baseQuestionId) {
        BaseQuestion baseQuestion = baseQuestionRepository.findById(baseQuestionId)
                .orElseThrow(() -> new NoSuchElementException("해당 질문을 찾을 수 없습니다. id=" + baseQuestionId));

        if (savedQuestionRepository.existsByUserAndBaseQuestion(user, baseQuestion)) {
            throw new IllegalArgumentException("이미 찜한 질문입니다.");
        }

        SavedQuestion savedQuestion = SavedQuestion.builder()
                .user(user)
                .baseQuestion(baseQuestion)
                .build();

        savedQuestionRepository.save(savedQuestion);
        return savedQuestion.getSqId();
    }

    private Long saveInterviewQuestion(User user, Long interviewQuestionId) {
        // TODO: interviewQuestionId로 실제 면접 질문 데이터가 있는지 검증하는 로직 필요
        // 예: interviewQuestionRepository.existsById(interviewQuestionId)

        if (savedQuestionRepository.existsByUserAndInterviewQuestionId(user, interviewQuestionId)) {
            throw new IllegalArgumentException("이미 찜한 질문입니다.");
        }

        SavedQuestion savedQuestion = SavedQuestion.builder()
                .user(user)
                .interviewQuestionId(interviewQuestionId)
                .build();

        savedQuestionRepository.save(savedQuestion);
        return savedQuestion.getSqId();
    }

    // 질문 찜하기 취소 (삭제)
    public void deleteSavedQuestion(User user, Long bqId, Long inqId) {
        if (bqId != null) {
            savedQuestionRepository.deleteByUserAndBaseQuestion_BqId(user, bqId);
        } else if (inqId != null) {
            savedQuestionRepository.deleteByUserAndInterviewQuestionId(user, inqId);
        } else {
            throw new IllegalArgumentException("찜 취소할 질문의 ID를 제공해야 합니다.");
        }
    }
}
