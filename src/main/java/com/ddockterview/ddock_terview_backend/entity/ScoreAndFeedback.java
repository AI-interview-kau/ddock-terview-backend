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
public class ScoreAndFeedback {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long scoreId;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "session_id")
    private Session session;

    private int totalScore;
    private int suitability;
    private int intendUnderstanding;
    private int problemSolving;
    private int accuracy;
    private int experience;
    private int logicality;
    private int confidence;
    private int speed;
    private int voice;
    private int gesture;
    private int attitude;
    private int gazing;

    @Column(columnDefinition = "TEXT")
    private String generalFeedback;

    @Column(columnDefinition = "TEXT")
    private String pro;

    @Column(columnDefinition = "TEXT")
    private String con;

    @CreatedDate
    private LocalDateTime createdAt;



}
