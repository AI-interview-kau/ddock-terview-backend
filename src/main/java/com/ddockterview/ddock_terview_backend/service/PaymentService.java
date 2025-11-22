package com.ddockterview.ddock_terview_backend.service;

import com.ddockterview.ddock_terview_backend.entity.enums.PaymentStatus;
import com.ddockterview.ddock_terview_backend.entity.Payment;
import com.ddockterview.ddock_terview_backend.entity.User;
import com.ddockterview.ddock_terview_backend.repository.PaymentRepository;
import com.siot.IamportRestClient.IamportClient;
import com.siot.IamportRestClient.exception.IamportResponseException;
import com.siot.IamportRestClient.response.IamportResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.IOException;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class PaymentService {

    private final PaymentRepository paymentRepository;
    private final IamportClient iamportClient;

    /**
     * 결제 준비 (사전 등록)
     * - DB에 PENDING 상태의 결제 정보를 미리 저장
     * - 생성된 merchantUid를 반환
     */
    @Transactional
    public String preparePayment(User user, Integer amount) {
        String merchantUid = UUID.randomUUID().toString(); // 고유한 주문번호 생성

        Payment payment = Payment.builder()
                .merchantUid(merchantUid)
                .amount(amount)
                .status(PaymentStatus.PENDING) // PENDING 상태로 저장
                .user(user)
                .itemTitle("프리미엄 이용권") // 예시
                .build();
        
        paymentRepository.save(payment);
        return merchantUid;
    }

    /**
     * 결제 검증
     */
    @Transactional
    public void verifyPayment(String paymentUid, String orderUid, User user) throws IamportResponseException, IOException {
        // 1. DB에서 주문 정보 조회
        Payment payment = paymentRepository.findByMerchantUid(orderUid)
                .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 주문입니다."));

        // 2. 사용자 검증: 주문 정보의 사용자와 현재 요청 사용자가 일치하는지 확인
        if (!payment.getUser().getUserId().equals(user.getUserId())) {
            throw new IllegalStateException("주문 정보의 사용자와 현재 사용자가 일치하지 않습니다.");
        }

        // 3. 멱등성 체크: 이미 처리된 결제인지 확인
        if (payment.getStatus() == PaymentStatus.PAID) {
            return; // 이미 결제 완료된 건이라면, 추가 처리 없이 성공으로 간주하고 종료
        }

        // 4. 포트원 서버에 결제 내역 조회
        IamportResponse<com.siot.IamportRestClient.response.Payment> iamportResponse = iamportClient.paymentByImpUid(paymentUid);

        if (iamportResponse.getResponse() == null) {
            throw new IllegalArgumentException("결제 내역이 존재하지 않습니다. paymentUid: " + paymentUid);
        }

        // 5. 금액 위변조 검증: DB에 저장된 금액과 실제 결제 금액 비교
        Integer expectedAmount = payment.getAmount(); // DB에서 조회한 예상 금액
        Integer actualAmount = iamportResponse.getResponse().getAmount().intValue();

        if (!expectedAmount.equals(actualAmount)) {
            // 금액이 일치하지 않으면 결제 실패 처리
            payment.fail();
            throw new IllegalStateException("결제 금액이 일치하지 않습니다.");
        }

        // 6. 결제 성공 처리
        payment.success(paymentUid);
        
        // TODO: 사용자에게 이용권을 부여하는 로직을 추가하세요.
    }
}
