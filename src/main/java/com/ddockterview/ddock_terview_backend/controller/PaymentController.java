package com.ddockterview.ddock_terview_backend.controller;

import com.ddockterview.ddock_terview_backend.dto.PaymentCallbackRequest;
import com.ddockterview.ddock_terview_backend.entity.User;
import com.ddockterview.ddock_terview_backend.service.PaymentService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/payment")
@RequiredArgsConstructor
public class PaymentController {

    private final PaymentService paymentService;

    /**
     * 결제 준비 API
     * @return 생성된 주문번호 (merchantUid)
     */
    @PostMapping("/prepare")
    public ResponseEntity<String> preparePayment(@AuthenticationPrincipal User user) {
        // 실제 서비스에서는 요청 DTO에서 상품 ID 등을 받아 금액을 결정해야 합니다.
        Integer amount = 3500; // 예시 금액
        String merchantUid = paymentService.preparePayment(user, amount);
        return ResponseEntity.ok(merchantUid);
    }

    /**
     * 결제 검증 API
     */
    @PostMapping("/validate")
    public ResponseEntity<String> validatePayment(@RequestBody PaymentCallbackRequest request,
                                                  @AuthenticationPrincipal User user) {
        try {
            paymentService.verifyPayment(request.getPaymentUid(), request.getOrderUid(), user);
            return ResponseEntity.ok("결제 검증 및 처리가 성공적으로 완료되었습니다.");
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }
}
