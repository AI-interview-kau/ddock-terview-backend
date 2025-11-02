package com.ddockterview.ddock_terview_backend.repository;

import com.ddockterview.ddock_terview_backend.entity.ScoreAndFeedback;
import com.ddockterview.ddock_terview_backend.entity.Session;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface ScoreAndFeedbackRepository extends JpaRepository<ScoreAndFeedback, Long> {

    Optional<ScoreAndFeedback> findBySession(Session session);

}
