package com.quiz.g4.entity;

import lombok.*;

import javax.persistence.*;
import java.time.LocalDate;
import java.util.Set;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity
@Table(name = "quizzes")
public class Quiz {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "quiz_id")
    private Integer quizId;

    @Column(name = "quiz_name", nullable = false)
    private String quizName;

    @Column(name = "description")
    private String description;

    @ManyToOne
    @JoinColumn(name = "created_by", nullable = false)
    private User createdBy;

    @Column(name = "created_date")
    private LocalDate createdDate = LocalDate.now();

    @Column(name = "updated_date")
    private LocalDate updatedDate = LocalDate.now();

    @OneToMany(mappedBy = "quiz")
    private Set<Question> questions;
}
