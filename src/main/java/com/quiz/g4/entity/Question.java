package com.quiz.g4.entity;

import com.quiz.g4.enums.QuestionType;
import lombok.*;

import javax.persistence.*;
import java.util.Set;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Builder
@Table(name = "questions")
public class Question {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "question_id")
    private Integer questionId;

    @ManyToOne
    @JoinColumn(name = "quiz_id", nullable = false)
    private Quiz quiz;

    @Column(name = "question_content", nullable = false)
    private String content;

    @Enumerated(EnumType.STRING)
    @Column(name = "question_type", nullable = false)
    private QuestionType questionType;  // Sử dụng enum cho kiểu câu hỏi

    @OneToMany(mappedBy = "question")
    private Set<AnswerOption> answerOptions;  // Các lựa chọn trả lời cho câu hỏi này


}
