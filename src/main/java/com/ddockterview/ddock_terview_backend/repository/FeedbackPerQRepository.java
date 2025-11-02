package com.ddockterview.ddock_terview_backend.repository;

import com.ddockterview.ddock_terview_backend.entity.FeedbackPerQ;
import com.ddockterview.ddock_terview_backend.entity.QuestionAfter;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface FeedbackPerQRepository extends JpaRepository<FeedbackPerQ, Long> {

    Optional<FeedbackPerQ> findByQuestionAfterIs(QuestionAfter questionAfter);

}
