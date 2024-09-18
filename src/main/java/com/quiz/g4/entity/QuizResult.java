package com.quiz.g4.entity;

import lombok.*;

import javax.persistence.*;
import java.time.LocalDate;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Builder
@Table(name = "quiz_results")
public class QuizResult {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "result_id")
    private Integer resultId;

    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false)
    private User user;  // Học sinh làm bài kiểm tra

    @ManyToOne
    @JoinColumn(name = "quiz_id", nullable = false)
    private Subject subject;  // Bài kiểm tra mà học sinh đã làm

    @Column(name = "total_score", nullable = false)
    private Double totalScore;  // Điểm tổng của học sinh

    @Column(name = "completed_at", nullable = false)
    private LocalDate completedAt = LocalDate.now();
}
