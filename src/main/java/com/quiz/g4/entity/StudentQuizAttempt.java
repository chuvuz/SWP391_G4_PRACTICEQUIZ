package com.quiz.g4.entity;

import lombok.*;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.Set;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity
@Table(name = "student_quiz_attempts")
public class StudentQuizAttempt {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "attempt_id")
    private Integer attemptId;

    @ManyToOne
    @JoinColumn(name = "quiz_id", nullable = false)
    private Quiz quiz;

    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @Column(name = "score")
    private Float score;

    @Column(name = "attempt_date", nullable = false)
    private LocalDateTime attemptDate = LocalDateTime.now();

    @Column(name = "completed_at")
    private LocalDateTime completedAt;

    @Column(name = "status", nullable = false)
    private String status;

    @OneToMany(mappedBy = "studentQuizAttempt")
    private Set<StudentAnswer> studentAnswers;
}
