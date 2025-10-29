package com.ddockterview.ddock_terview_backend.service;

import com.ddockterview.ddock_terview_backend.dto.qlist.QuestionCategoryResponseDto;
import com.ddockterview.ddock_terview_backend.dto.qlist.QuestionItemDto;
import com.ddockterview.ddock_terview_backend.dto.qlist.QuestionListResponseDto;
import com.ddockterview.ddock_terview_backend.entity.BaseQuestion;
import com.ddockterview.ddock_terview_backend.entity.User;
import com.ddockterview.ddock_terview_backend.entity.enums.Category;
import com.ddockterview.ddock_terview_backend.entity.enums.Origin;
import com.ddockterview.ddock_terview_backend.repository.BaseQuestionRepository;
import com.ddockterview.ddock_terview_backend.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional
public class BaseQuestionService {

    private final BaseQuestionRepository questionRepository;
    private final UserRepository userRepository;

    public QuestionListResponseDto getQuestionList(String userId) {

        // 1. 사용자 조회
        User user = userRepository.findByUserId(userId)
                .orElseThrow(() -> new IllegalArgumentException("사용자를 찾을 수 없습니다: " + userId));

        // 2. [SYSTEM] 질문 전체 조회
        List<BaseQuestion> systemQuestions = questionRepository.findByOrigin(Origin.SYSTEM);

        // 3. [USER] 질문 (로그인한 사용자의 것) 조회
        List<BaseQuestion> userQuestions = questionRepository.findByOriginAndUser(Origin.USER, user);

        // 4. API 명세에 맞게 3개의 키(PERSONALITY, TECH, MINE)로 데이터 가공
        Map<String, QuestionCategoryResponseDto> categoriesMap = new HashMap<>();

        // 4-1. "PERSONALITY" (SYSTEM 질문 중 Category가 PERSONALITY인 것)
        List<QuestionItemDto> personalityDtos = systemQuestions.stream()
                .filter(q -> q.getCategory() == Category.PERSONALITY)
                .map(QuestionItemDto::new) // .map(bq -> new QuestionItemDto(bq))
                .collect(Collectors.toList());
        categoriesMap.put("PERSONALITY", new QuestionCategoryResponseDto(personalityDtos));

        // 4-2. "TECH" (SYSTEM 질문 중 Category가 TECH인 것)
        List<QuestionItemDto> techDtos = systemQuestions.stream()
                .filter(q -> q.getCategory() == Category.TECH)
                .map(QuestionItemDto::new)
                .collect(Collectors.toList());
        categoriesMap.put("TECH", new QuestionCategoryResponseDto(techDtos));

        // 4-3. "MINE" (조회한 USER 질문 전체)
        List<QuestionItemDto> mineDtos = userQuestions.stream()
                .map(QuestionItemDto::new)
                .collect(Collectors.toList());
        categoriesMap.put("MINE", new QuestionCategoryResponseDto(mineDtos));

        // 5. 최종 DTO로 감싸서 반환
        return new QuestionListResponseDto(categoriesMap);
    }




}
