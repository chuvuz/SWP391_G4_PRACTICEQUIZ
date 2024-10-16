package com.quiz.g4.entity;

import lombok.*;

import javax.persistence.Entity;

import javax.persistence.*;
import java.time.LocalDateTime;

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
    private User user;

    @ManyToOne
    @JoinColumn(name = "quiz_id", nullable = false)
    private Quiz quiz;

    @Column(name = "score", nullable = false)
    private Double score;  // Điểm số của học sinh

    @Column(name = "completed_at", nullable = false)
    private LocalDateTime completedAt;  // Thời gian hoàn thành

//    public Integer getLessonId() {
//        return this.lesson != null ? this.lesson.getLessonId() : null;
//    }
}
