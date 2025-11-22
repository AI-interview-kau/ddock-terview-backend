package com.ddockterview.ddock_terview_backend.service;

import com.ddockterview.ddock_terview_backend.domain.PaymentStatus;
import com.ddockterview.ddock_terview_backend.entity.Payment;
import com.ddockterview.ddock_terview_backend.repository.PaymentRepository;
import com.siot.IamportRestClient.IamportClient;
import com.siot.IamportRestClient.exception.IamportResponseException;
import com.siot.IamportRestClient.response.IamportResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.IOException;

@Service
@RequiredArgsConstructor
public class PaymentService {

    private final PaymentRepository paymentRepository;
    private final IamportClient iamportClient;

    // 결제 검증 로직
    @Transactional
    public void verifyPayment(String paymentUid, String orderUid) throws IamportResponseException, IOException {
        // 1. 포트원 서버에 결제 내역 조회 요청
        IamportResponse<com.siot.IamportRestClient.response.Payment> iamportResponse = iamportClient.paymentByImpUid(paymentUid);

        // 2. 조회된 결제 정보가 없는 경우 예외 발생
        if (iamportResponse.getResponse() == null) {
            throw new IllegalArgumentException("결제 내역이 존재하지 않습니다. paymentUid: " + paymentUid);
        }

        // 3. 결제 금액 위변조 검증
        // 실제 서비스에서는 DB에서 주문 정보를 조회하여 금액을 비교해야 합니다.
        // 여기서는 요청에 명시된 대로 고정된 금액(예: 1000원)으로 비교합니다.
        Integer expectedAmount = 3500; // 예시: 이용권 가격
        Integer actualAmount = iamportResponse.getResponse().getAmount().intValue();

        if (!expectedAmount.equals(actualAmount)) {
            // 결제 금액이 일치하지 않으면 결제 실패 처리 및 예외 발생
            saveFailedPayment(orderUid, paymentUid, actualAmount, "결제 금액 위변조 시도");
            throw new IllegalStateException("결제 금액이 일치하지 않습니다.");
        }

        // 4. 결제 성공 처리
        // DB에 결제 정보를 저장하고 상태를 'PAID'로 변경합니다.
        saveSuccessfulPayment(orderUid, paymentUid, actualAmount);
        
        // TODO: 사용자에게 이용권을 부여하는 로직을 추가하세요.
    }

    private void saveSuccessfulPayment(String orderUid, String paymentUid, Integer amount) {
        Payment payment = Payment.builder()
                .merchantUid(orderUid)
                .impUid(paymentUid)
                .itemTitle("프리미엄 이용권") // 예시 상품명
                .amount(amount)
                .status(PaymentStatus.PAID)
                .build();
        paymentRepository.save(payment);
    }

    private void saveFailedPayment(String orderUid, String paymentUid, Integer amount, String reason) {
        Payment payment = Payment.builder()
                .merchantUid(orderUid)
                .impUid(paymentUid)
                .itemTitle("프리미엄 이용권 - 결제 실패")
                .amount(amount)
                .status(PaymentStatus.FAILED)
                .build();
        paymentRepository.save(payment);
    }
}
