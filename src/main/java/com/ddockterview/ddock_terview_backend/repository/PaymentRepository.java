package com.ddockterview.ddock_terview_backend.repository;

import com.ddockterview.ddock_terview_backend.entity.Payment;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface PaymentRepository extends JpaRepository<Payment, Long> {
    // 주문 번호로 결제 정보를 조회하는 메서드
    Optional<Payment> findByMerchantUid(String merchantUid);
}
