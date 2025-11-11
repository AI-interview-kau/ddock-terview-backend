package com.ddockterview.ddock_terview_backend.service;

import com.ddockterview.ddock_terview_backend.dto.qlist.MyQuestionRequestDto;
import com.ddockterview.ddock_terview_backend.dto.qlist.MyQuestionResponseDto;
import com.ddockterview.ddock_terview_backend.dto.savedQ.SaveQuestionListDto;
import com.ddockterview.ddock_terview_backend.dto.savedQ.SaveQuestionRequestDto;
import com.ddockterview.ddock_terview_backend.dto.savedQ.SaveQuestionResponseDto;
import com.ddockterview.ddock_terview_backend.entity.BaseQuestion;
import com.ddockterview.ddock_terview_backend.entity.QuestionAfter;
import com.ddockterview.ddock_terview_backend.entity.SavedQuestion;
import com.ddockterview.ddock_terview_backend.entity.User;
import com.ddockterview.ddock_terview_backend.entity.enums.Origin;
import com.ddockterview.ddock_terview_backend.repository.BaseQuestionRepository;
import com.ddockterview.ddock_terview_backend.repository.QuestionAfterRepository;
import com.ddockterview.ddock_terview_backend.repository.SavedQuestionRepository;
import com.ddockterview.ddock_terview_backend.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.NoSuchElementException;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional
public class QuestionListService {

    private final BaseQuestionRepository baseQuestionRepository;
    private final SavedQuestionRepository savedQuestionRepository;
    private final UserRepository userRepository;
    private final QuestionAfterRepository questionAfterRepository;

    // 내가 만든 질문 저장
    public MyQuestionResponseDto createMyQuestion(String userId, MyQuestionRequestDto requestDto) {
        User user = userRepository.findByUserId(userId)
                .orElseThrow(() -> new IllegalArgumentException("사용자를 찾을 수 없습니다."));

        BaseQuestion newQuestion = requestDto.toEntity(user);
        baseQuestionRepository.save(newQuestion);

        return new MyQuestionResponseDto(newQuestion);
    }

    // 내가 만든 질문 삭제
    public void deleteMyQuestion(String userId, Long bqId) {
        User user = userRepository.findByUserId(userId)
                .orElseThrow(() -> new IllegalArgumentException("사용자를 찾을 수 없습니다."));

        BaseQuestion question = baseQuestionRepository.findById(bqId)
                .orElseThrow(() -> new NoSuchElementException("해당 질문을 찾을 수 없습니다. id=" + bqId));

        if (question.getUser() == null || !question.getUser().getUserId().equals(user.getUserId())) {
            throw new SecurityException("질문을 삭제할 권한이 없습니다.");
        }

        if (question.getOrigin() != Origin.USER) {
            throw new IllegalArgumentException("사용자가 만든 질문만 삭제할 수 있습니다.");
        }

        // 이 질문을 찜한 모든 기록을 삭제
        savedQuestionRepository.deleteByUserAndBaseQuestion_BqId(user, bqId);
        baseQuestionRepository.delete(question);
    }

    // 질문 찜하기 (저장)
    public SaveQuestionResponseDto saveQuestion(String userId, SaveQuestionRequestDto requestDto) {

        User user = userRepository.findByUserId(userId)
                .orElseThrow(() -> new IllegalArgumentException("사용자를 찾을 수 없습니다."));

        if (requestDto.getBqId() != null && requestDto.getBqId() > 0) {
            return saveBaseQuestion(user, requestDto.getBqId());
        } else if (requestDto.getInqId() != null) {
            return saveInterviewQuestion(user, requestDto.getInqId());
        } else {
            throw new IllegalArgumentException("찜할 질문의 ID를 제공해야 합니다.");
        }

    }

    private SaveQuestionResponseDto saveBaseQuestion(User user, Long baseQuestionId) {
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

        return new SaveQuestionResponseDto(savedQuestion);
    }

    private SaveQuestionResponseDto saveInterviewQuestion(User user, Long interviewQuestionId) {
        // TODO: interviewQuestionId로 실제 면접 질문 데이터가 있는지 검증하는 로직 필요
        // 예: interviewQuestionRepository.existsById(interviewQuestionId)

        QuestionAfter interviewQuestion = questionAfterRepository.findById(interviewQuestionId)
                .orElseThrow(() -> new NoSuchElementException("해당 질문을 찾을 수 없습니다. id=" + interviewQuestionId));

        if (savedQuestionRepository.existsByUserAndQuestionAfterIs(user,interviewQuestion)) {
            throw new IllegalArgumentException("이미 찜한 질문입니다.");
        }

        SavedQuestion savedQuestion = SavedQuestion.builder()
                .user(user)
                .questionAfter(interviewQuestion)
                .build();

        savedQuestionRepository.save(savedQuestion);
        return new SaveQuestionResponseDto(savedQuestion);
    }

    // 질문 찜하기 취소 (삭제)
    public void deleteSavedQuestion(String userId, Long bqId, Long inqId) {
        User user = userRepository.findByUserId(userId)
                .orElseThrow(() -> new IllegalArgumentException("사용자를 찾을 수 없습니다."));

        if (bqId != null && bqId > 0) {
            savedQuestionRepository.deleteByUserAndBaseQuestion_BqId(user, bqId);
        } else if (inqId != null) {
            savedQuestionRepository.deleteByUserAndQuestionAfter_InqId(user, inqId);
        } else {
            throw new IllegalArgumentException("찜 취소할 질문의 ID를 제공해야 합니다.");
        }
    }

    // 찜한 질문 목록 조회
    @Transactional(readOnly = true)
    public SaveQuestionListDto getSavedQuestions(String userId) {
        User user = userRepository.findByUserId(userId)
                .orElseThrow(() -> new IllegalArgumentException("사용자를 찾을 수 없습니다."));

        List<SavedQuestion> savedQuestions = savedQuestionRepository.findAllByUser(user);

        List<String> contents = savedQuestions.stream()
                .map(savedQuestion -> {
                    if (savedQuestion.getBaseQuestion() != null) {
                        return savedQuestion.getBaseQuestion().getContent();
                    } else if (savedQuestion.getQuestionAfter() != null) {
                        return savedQuestion.getQuestionAfter().getContent();
                    }
                    return null;
                })
                .collect(Collectors.toList());

        return new SaveQuestionListDto(contents);
    }
}
