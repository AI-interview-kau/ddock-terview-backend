package com.ddockterview.ddock_terview_backend.entity;

import jakarta.persistence.*;
import lombok.*;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.time.LocalDateTime;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Builder
@EntityListeners(AuditingEntityListener.class)
@Entity
public class FeedbackPerQ {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long fqId;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "inq_id", unique = true)
    private QuestionAfter questionAfter;

    @Column(columnDefinition = "TEXT")
    private String behavefeedback;

    @Column(columnDefinition = "TEXT")
    private String langfeedback;

    @CreatedDate
    private LocalDateTime createdAt;


}
