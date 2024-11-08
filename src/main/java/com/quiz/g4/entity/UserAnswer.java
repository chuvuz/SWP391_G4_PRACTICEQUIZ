package com.quiz.g4.entity;

import lombok.*;

import javax.persistence.*;

@Entity
@Builder
@Table(name = "user_answers")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class UserAnswer {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "answer_id")
    private Integer answerId;

    @ManyToOne
    @JoinColumn(name = "result_id", nullable = false)
    private QuizResult quizResult;  // Link to quiz result

    @ManyToOne
    @JoinColumn(name = "question_id", nullable = false)
    private QuestionBank question;  // Link to question bank

    @ManyToOne
    @JoinColumn(name = "selected_option_id", nullable = false)
    private AnswerOption selectedAnswer;  // Student's selected answer

    @Column(name = "is_correct", nullable = false)
    private Boolean isCorrect;  // Determines correct/incorrect
}

