package com.quiz.g4.entity;

import lombok.*;

import javax.persistence.*;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity
@Table(name = "student_answers")
public class StudentAnswer {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "student_answer_id")
    private Integer studentAnswerId;

    @ManyToOne
    @JoinColumn(name = "record_id", nullable = false)
    private RecordStudent recordStudent;

    @ManyToOne
    @JoinColumn(name = "question_id", nullable = false)
    private Question question;

    @ManyToOne
    @JoinColumn(name = "selected_answer_id")
    private Answer selectedAnswer;

    @Column(name = "numeric_answer")
    private Double numericAnswer;

    @Column(name = "is_correct")
    private Boolean isCorrect;
}
