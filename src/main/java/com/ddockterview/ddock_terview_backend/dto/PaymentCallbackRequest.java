package com.ddockterview.ddock_terview_backend.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class PaymentCallbackRequest {

    @JsonProperty("payment_uid")
    private String paymentUid; // 포트원 결제 고유 번호 (imp_uid)

    @JsonProperty("order_uid")
    private String orderUid;   // 우리 시스템의 주문 번호 (merchant_uid)
}
