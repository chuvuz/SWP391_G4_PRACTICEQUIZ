package com.quiz.g4.entity;

import lombok.*;

import javax.persistence.*;
import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Builder
@Table(name = "feedback")
public class Feedback {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "feedback_id")
    private Integer feedbackId;

    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false)
    private User user;  // Người dùng đưa ra phản hồi

    @ManyToOne
    @JoinColumn(name = "quiz_id", nullable = false)
    private Quiz quiz;  // Bài kiểm tra được phản hồi

    @Column(name = "rating", nullable = false)
    private Integer rating;  // Đánh giá, có thể là thang điểm từ 1 đến 5

    @Column(name = "comments", columnDefinition = "TEXT")
    private String comments;  // Phản hồi của người dùng

    @Column(name = "created_at", nullable = false)
    private LocalDateTime createdAt = LocalDateTime.now();  // Thời gian phản hồi
}
