package com.quiz.g4.entity;
import lombok.*;

import javax.persistence.*;
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
    private Integer quizId;

    @Column(name = "quiz_name", nullable = false)
    private String quizName;

    @ManyToOne
    @JoinColumn(name = "created_by", nullable = false)
    private User createdBy;

    @OneToMany(mappedBy = "quiz")
    private Set<Lesson> lessons;  // Liên kết với các bài học

    @ManyToOne
    @JoinColumn(name = "subject_id", nullable = false)  // Liên kết với bảng Subject
    private Subject subject;
}