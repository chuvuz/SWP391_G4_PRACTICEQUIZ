package com.quiz.g4.entity;

import lombok.*;

import javax.persistence.Entity;
import lombok.*;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.Set;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Builder
@Table(name = "lesson_results")
public class LessonResult {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "result_id")
    private Integer resultId;

    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @ManyToOne
    @JoinColumn(name = "lesson_id", nullable = false)
    private Lesson lesson;

    @Column(name = "score", nullable = false)
    private Double score;  // Điểm số của học sinh

    @Column(name = "completed_at", nullable = false)
    private LocalDateTime completedAt;  // Thời gian hoàn thành
}
