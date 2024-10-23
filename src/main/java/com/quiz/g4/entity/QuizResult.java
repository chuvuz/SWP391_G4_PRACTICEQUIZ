package com.quiz.g4.entity;

import lombok.*;

import javax.persistence.Entity;

import javax.persistence.*;
import java.time.LocalDateTime;

@Entity
@Builder
@Table(name = "quiz_results")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class QuizResult {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "result_id")
    private Integer resultId;

    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false)
    private User user;  // Link to user

    @ManyToOne
    @JoinColumn(name = "quiz_id", nullable = false)
    private Quiz quiz;  // Link to quiz

    @Column(name = "score", nullable = false)
    private Double score;  // Student's score

    @Column(name = "completed_at", nullable = false)
    private LocalDateTime completedAt;  // Completion time
}

