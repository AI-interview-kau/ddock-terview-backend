package com.ddockterview.ddock_terview_backend.entity;

import jakarta.persistence.*;
import lombok.*;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.time.LocalDateTime;

@Entity
@Getter
@NoArgsConstructor
@EntityListeners(AuditingEntityListener.class)
@Builder
@AllArgsConstructor
@Table(name = "saved_question")
public class SavedQuestion {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long sqId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id")
    private BaseQuestion baseQuestion;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "inq_id")
    private QuestionAfter questionAfter;

    @CreatedDate
    @Column(updatable = false)
    private LocalDateTime createdAt;


}
