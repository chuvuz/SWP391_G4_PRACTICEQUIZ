package com.quiz.g4.entity;

import lombok.*;

import javax.persistence.*;
import java.util.Set;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity
@Table(name = "questions")
public class Question {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "question_id")
    private Integer questionId;

    @Column(name = "content", nullable = false)
    private String content;

    @ManyToOne
    @JoinColumn(name = "quiz_id", nullable = false)
    private Quiz quiz;

    @Column(name = "question_type", nullable = false)
    private String questionType;

    @Column(name = "correct_numeric_answer")
    private Double correctNumericAnswer;

    @OneToMany(mappedBy = "question")
    private Set<Answer> answers;
}
