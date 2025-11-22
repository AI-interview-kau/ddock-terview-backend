package com.ddockterview.ddock_terview_backend.controller;

import com.ddockterview.ddock_terview_backend.dto.PaymentCallbackRequest;
import com.ddockterview.ddock_terview_backend.service.PaymentService;
import com.siot.IamportRestClient.exception.IamportResponseException;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;

@RestController
@RequestMapping("/api/payment")
@RequiredArgsConstructor
public class PaymentController {

    private final PaymentService paymentService;

    @PostMapping("/validate")
    public ResponseEntity<String> validatePayment(@RequestBody PaymentCallbackRequest request) {
        try {
            paymentService.verifyPayment(request.getPaymentUid(), request.getOrderUid());
            return ResponseEntity.ok("결제 검증 및 처리가 성공적으로 완료되었습니다.");
        } catch (IamportResponseException | IOException e) {
            // 포트원 API 통신 중 에러 발생 시
            return ResponseEntity.status(500).body("결제 검증 중 오류가 발생했습니다: " + e.getMessage());
        } catch (IllegalStateException | IllegalArgumentException e) {
            // 자체 검증 로직에서 에러 발생 시 (예: 금액 불일치)
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }
}
