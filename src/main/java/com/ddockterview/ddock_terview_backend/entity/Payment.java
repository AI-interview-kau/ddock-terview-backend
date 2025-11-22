package com.ddockterview.ddock_terview_backend.entity;

import com.ddockterview.ddock_terview_backend.entity.enums.PaymentStatus;
import jakarta.persistence.*;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;

import java.time.LocalDateTime;

@Entity
@Getter
@NoArgsConstructor
public class Payment {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String itemTitle; // 상품명

    @Column(unique = true) // nullable = true, 결제 성공 시 채워짐
    private String impUid; // 포트원 결제 고유 번호

    @Column(nullable = false, unique = true)
    private String merchantUid; // 우리 시스템의 주문 번호

    @Column(nullable = false)
    private Integer amount; // 결제 금액

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private PaymentStatus status; // 결제 상태

    @CreationTimestamp
    private LocalDateTime paymentDate; // 결제일

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @Builder
    public Payment(String merchantUid, Integer amount, PaymentStatus status, User user, String itemTitle) {
        this.merchantUid = merchantUid;
        this.amount = amount;
        this.status = status;
        this.user = user;
        this.itemTitle = itemTitle;
    }

    // 결제 성공 시 상태 변경
    public void success(String impUid) {
        this.impUid = impUid;
        this.status = PaymentStatus.PAID;
    }

    // 결제 실패 시 상태 변경
    public void fail() {
        this.status = PaymentStatus.FAILED;
    }
}
