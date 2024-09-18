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

    @Column(name = "question_content", nullable = false)
    private String content;

    @Enumerated(EnumType.STRING)
    @Column(name = "question_type", nullable = false)
    private QuestionType questionType;  // Sử dụng enum cho kiểu câu hỏi

    @OneToMany(mappedBy = "question")
<<<<<<< Updated upstream
    private Set<AnswerOption> answerOptions;  // Các lựa chọn trả lời cho câu hỏi này
=======
    private Set<QuizQuestion> quizQuestions;  // Các lựa chọn trả lời cho câu hỏi này


>>>>>>> Stashed changes
}
