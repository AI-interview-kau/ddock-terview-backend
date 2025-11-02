package com.ddockterview.ddock_terview_backend.entity;

import jakarta.persistence.*;
import lombok.*;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.time.LocalDateTime;

@Getter
@Setter
@Builder
@EntityListeners(AuditingEntityListener.class)
@NoArgsConstructor
@AllArgsConstructor
@Entity
public class QuestionAfter {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long inqId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "session_id")
    private Session session;

    @Column(nullable = false)
    private String content;

    private Boolean isTailQ;
    private String s3key;
    private String answer;

    @CreatedDate
    private LocalDateTime createdAt;



}
