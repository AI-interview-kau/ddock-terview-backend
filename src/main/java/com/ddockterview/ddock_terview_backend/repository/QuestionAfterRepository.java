package com.ddockterview.ddock_terview_backend.repository;

import com.ddockterview.ddock_terview_backend.entity.QuestionAfter;
import com.ddockterview.ddock_terview_backend.entity.Session;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface QuestionAfterRepository extends JpaRepository<QuestionAfter, Long> {

    List<QuestionAfter> findAllBySession(Session session);

}
