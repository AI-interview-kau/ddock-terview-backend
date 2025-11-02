package com.ddockterview.ddock_terview_backend.repository;

import com.ddockterview.ddock_terview_backend.entity.BaseQuestion;
import com.ddockterview.ddock_terview_backend.entity.SavedQuestion;
import com.ddockterview.ddock_terview_backend.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

public interface SavedQuestionRepository extends JpaRepository<SavedQuestion, Long> {

    boolean existsByUserAndBaseQuestion(User user, BaseQuestion baseQuestion);

    @Transactional
    void deleteByUserAndBaseQuestion_BqId(User user, Long bqId);

    boolean existsByUserAndInterviewQuestionId(User user, Long interviewQuestionId);

    @Transactional
    void deleteByUserAndInterviewQuestionId(User user, Long interviewQuestionId);
}
