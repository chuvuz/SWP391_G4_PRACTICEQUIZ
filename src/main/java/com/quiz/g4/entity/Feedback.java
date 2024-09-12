package com.quiz.g4.entity;

import com.quiz.g4.enums.CommentStatus;
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

    @Column(name = "quiz_id")
    private Integer quizId; // Bài kiểm tra được phản hồi

    @Column(name = "blog_id")
    private Integer blogId; //Blog được phản hồi

    @Column(name = "comments", columnDefinition = "TEXT")
    private String comments;  // Phản hồi của người dùng

    @Enumerated(EnumType.STRING)
    @Column(name = "status", nullable = false)
    private CommentStatus status = CommentStatus.PENDING;  // Trạng thái của phản hồi

    @Column(name = "created_at", nullable = false)
    private LocalDateTime createdAt = LocalDateTime.now();  // Thời gian phản hồi
}
