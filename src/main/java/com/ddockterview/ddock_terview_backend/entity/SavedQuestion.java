package com.ddockterview.ddock_terview_backend.entity;

import jakarta.persistence.*;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Entity
@Getter
@NoArgsConstructor
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

    @Column(name = "inq_id")
    private Long interviewQuestionId;

    private LocalDateTime createdAt;

    @Builder
    public SavedQuestion(User user, BaseQuestion baseQuestion, Long interviewQuestionId) {
        this.user = user;
        this.baseQuestion = baseQuestion;
        this.interviewQuestionId = interviewQuestionId;
        this.createdAt = LocalDateTime.now();
    }
}
