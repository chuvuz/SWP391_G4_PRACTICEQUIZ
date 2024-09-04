package com.quiz.g4.entity;

import lombok.*;

import javax.persistence.*;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity
@Table(name = "answers")
public class Answer {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "answer_id")
    private Integer answerId;

    @Column(name = "content", nullable = false)
    private String content;

    @Column(name = "is_correct", nullable = false)
    private Boolean isCorrect;

    @ManyToOne
    @JoinColumn(name = "question_id", nullable = false)
    private Question question;
}
