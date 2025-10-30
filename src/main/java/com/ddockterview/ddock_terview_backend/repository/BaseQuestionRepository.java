package com.ddockterview.ddock_terview_backend.repository;

import com.ddockterview.ddock_terview_backend.entity.BaseQuestion;
import com.ddockterview.ddock_terview_backend.entity.User;
import com.ddockterview.ddock_terview_backend.entity.enums.Origin;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface BaseQuestionRepository extends JpaRepository<BaseQuestion, Long> {

    // 1. Origin이 SYSTEM인 모든 질문을 조회
    List<BaseQuestion> findByOrigin(Origin origin);

    // 2. Origin이 USER이면서 특정 사용자에 해당하는 질문만 조회
    List<BaseQuestion> findByOriginAndUser(Origin origin, User user);

}
