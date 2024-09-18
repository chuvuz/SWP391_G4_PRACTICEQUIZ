package com.quiz.g4.entity;

import lombok.*;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Table;
import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.Set;
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Builder
@Table(name = "quizzes")
public class Quiz {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "quiz_id")
    private Long quizId;

    @Column(name = "quiz_name", nullable = false)
    private String quizName;

    @Column(name = "created_by", nullable = false)
    private Long createdBy; // Assuming this is the userId

    @Column(name = "created_at", nullable = false)
    private LocalDateTime createdAt;

<<<<<<< Updated upstream
=======
    @Column(name = "is_delete", nullable = false)
    private Boolean isDelete;

    @OneToMany(mappedBy = "quiz")
    private Set<QuizQuestion> quizQuestions;
>>>>>>> Stashed changes
}
